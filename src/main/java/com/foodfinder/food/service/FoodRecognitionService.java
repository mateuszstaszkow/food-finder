package com.foodfinder.food.service;

import com.foodfinder.food.domain.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
@Slf4j
@PropertySource("classpath:secrets.properties")
public class FoodRecognitionService {

    private final FoodLiveSearchService liveSearchService;

    private static final int FEATURE_MAX_RESULTS = 5;
    private static final int MAX_FILE_SIZE = 128000;
    private static final int LIVE_SEARCH_PAGE_SIZE = 10;
    private static final String FEATURE_TYPE = "LABEL_DETECTION";
    private static final String[] FEATURE_BANNED_TYPES = {"food", "vegetable", "produce", "fruit", "product", "dish"};
    private static final String NOT_FOUND_PRODUCT = "Spaghetti, with meat";

    @Value("${google.vision.url}")
    private String GOOGLE_VISION_URL;

    @Value("${google.translate.auth}")
    private String GOOGLE_AUTH;

    public ProductDTO recognizeFood(MultipartFile file) {
        return Optional.ofNullable(file)
                .map(this::encodeToBase64)
                .map(this::buildRequest)
                .map(this::getRecognitionData)
                .map(this::findBestMatchingProduct) // TODO improve
                .orElseThrow(BadRequestException::new);
    }

    private String encodeToBase64(MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();
            byte[] encoded = Base64.getEncoder().encode(fileBytes);
            return new String(encoded);
        }
        catch (IOException exception) {
            log.error("Could not get bytes from file I/O Exception", exception);
            throw new BadRequestException(exception);
        }
    }

    private List<GoogleVisionLabelAnnotationDTO> getRecognitionData(GoogleVisionMainRequestDTO request) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            return restTemplate.postForObject(buildUri(), request, GoogleVisionMainResponseDTO.class)
                    .getResponses()
                    .get(0)
                    .getLabelAnnotations();
        }
        catch (RestClientException exception) {
            int contentLength = request.getRequests()
                    .get(0)
                    .getImage()
                    .getContent()
                    .length();
            log.error("Google Cloud Vision REST API error" +
                    (contentLength > MAX_FILE_SIZE ? ", file is probably too large" : ""), exception);

            throw new BadRequestException(exception);
        }
    }

    private ProductDTO findBestMatchingProduct(List<GoogleVisionLabelAnnotationDTO> recognitionData) {
        Boolean descriptionValid = !recognitionData.isEmpty() && ( recognitionData.get(0) != null );
        String productName = descriptionValid ? recognitionData.get(0).getDescription() : NOT_FOUND_PRODUCT;

        if(isNameBanned(productName)) {
            return deeperMatching(recognitionData);
        }

        List<ProductDTO> products = liveSearchService.getOriginalProducts(productName, LIVE_SEARCH_PAGE_SIZE);
        List<ProductDTO> filteredProducts = products.stream()
                .filter(product -> isAProperName(product, productName))
                .collect(Collectors.toList());

        log.info("Food recognition (" + productName + "): found " + products.size()
                + " products (filtered: " + filteredProducts.size() + ")");

        if(filteredProducts.isEmpty() && !products.isEmpty()) {
            return products.get(0);
        } else if(filteredProducts.isEmpty() && products.isEmpty()) {
            return deeperMatching(recognitionData);
        }
        return filteredProducts.get(0);
    }

    private ProductDTO deeperMatching(List<GoogleVisionLabelAnnotationDTO> recognitionData) {
        List<ProductDTO> products = new ArrayList<>();
        List<ProductDTO> filteredProducts = new ArrayList<>();

        recognitionData.stream()
                .map(GoogleVisionLabelAnnotationDTO::getDescription)
                .filter(description -> !isNameBanned(description))
                .forEach(productName -> {
                    products.addAll(liveSearchService.getOriginalProducts(productName, LIVE_SEARCH_PAGE_SIZE));
                    filteredProducts.addAll(products.stream()
                            .filter(product -> isAProperName(product, productName))
                            .collect(Collectors.toList()));
                });

        logDeeperMatching(recognitionData, products, filteredProducts);

        if(filteredProducts.isEmpty() && !products.isEmpty()) {
            return products.get(0);
        } else if(filteredProducts.isEmpty() && products.isEmpty()) {
            return liveSearchService.getOriginalProducts(NOT_FOUND_PRODUCT, 1).get(0);
        }

        return filteredProducts.get(0);
    }

    private Boolean isNameBanned(String productName) {
        Boolean nameBanned = false;

        for (String FEATURE_BANNED_TYPE : FEATURE_BANNED_TYPES) {
            if(productName.contains(FEATURE_BANNED_TYPE)) {
                nameBanned = true;
            }
        }

        return nameBanned;
    }

    private URI buildUri() {
        return UriComponentsBuilder.fromUriString(GOOGLE_VISION_URL)
                .queryParam("key", GOOGLE_AUTH)
                .build()
                .toUri();
    }

    private GoogleVisionMainRequestDTO buildRequest(String base64Graphic) {
        GoogleVisionMainRequestDTO mainRequestDTO = new GoogleVisionMainRequestDTO();
        GoogleVisionRequestDTO requestDTO = new GoogleVisionRequestDTO();
        GoogleVisionImageDTO imageDTO = new GoogleVisionImageDTO();
        imageDTO.setContent(base64Graphic);
        GoogleVisionFeatureDTO featureDTO = new GoogleVisionFeatureDTO();
        featureDTO.setMaxResults(FEATURE_MAX_RESULTS);
        featureDTO.setType(FEATURE_TYPE);

        requestDTO.setImage(imageDTO);
        requestDTO.setFeatures(Collections.singletonList(featureDTO));
        mainRequestDTO.setRequests(Collections.singletonList(requestDTO));

        return mainRequestDTO;
    }

    private Boolean isAProperName(ProductDTO product, String recognizedName) {
        recognizedName = recognizedName.toLowerCase();
        String productName = product.getName().toLowerCase();

        Boolean containSpace = productName.startsWith(recognizedName + " ");
        Boolean containComma = productName.startsWith(recognizedName + ",");
        Boolean containDot = productName.startsWith(recognizedName + ".");
        Boolean equal = productName.equals(recognizedName);
        Boolean containSpacePlural = productName.startsWith(recognizedName + "s ");
        Boolean containCommaPlural = productName.startsWith(recognizedName + "s,");
        Boolean containDotPlural = productName.startsWith(recognizedName + "s.");
        Boolean equalPlural = productName.equals(recognizedName + "s");

        return  containSpace || containComma || containDot || equal ||
                containSpacePlural || containCommaPlural || containDotPlural || equalPlural;
    }

    private void logDeeperMatching(List<GoogleVisionLabelAnnotationDTO> recognitionData, List<ProductDTO> products,
                                   List<ProductDTO> filteredProducts) {

        String labels = recognitionData.stream()
                .map(GoogleVisionLabelAnnotationDTO::getDescription)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        log.info("Food recognition, deeper matching (" + labels + "): found " + products.size()
                + " products (filtered: " + filteredProducts.size() + ")");
    }
}