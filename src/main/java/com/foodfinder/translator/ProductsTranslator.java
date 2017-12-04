package com.foodfinder.translator;

import com.foodfinder.translator.dto.TranslateResponseDTO;
import com.foodfinder.food.domain.Product;
import com.foodfinder.food.domain.ProductRepository;
import com.google.common.math.IntMath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.RoundingMode;
import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional
class ProductsTranslator {

    private final ProductRepository productRepository;

    @Value("${google.translate.url}")
    private String TRANSLATE_URL;

    @Value("${google.translate.auth}")
    private String GOOGLE_AUTH;

    private static final int PAGE_SIZE = 1500;

    void translate() {
        RestTemplate restTemplate = new RestTemplate();
        AtomicInteger counter = new AtomicInteger(0);
        int productsCount = (int) productRepository.count();
        int numberOfPages = IntMath.divide(productsCount, PAGE_SIZE, RoundingMode.CEILING);

        IntStream.range(0, numberOfPages)
                .forEach(page -> {
                    Page<Product> products = productRepository.findAll(new PageRequest(page, PAGE_SIZE))
                            .map(product -> translateSingleProduct(product, restTemplate, counter, productsCount));

                    productRepository.save(products);
                });
    }

    private Product translateSingleProduct(Product product, RestTemplate restTemplate,
                                           AtomicInteger counter, int productsCount) {

        try {
            String translation = restTemplate.getForObject(buildUri(product.getName()), TranslateResponseDTO.class)
                    .getData()
                    .getTranslations()
                    .get(0)
                    .getTranslatedText();

            logCurrentProgress(counter, productsCount);
            return product.setName(translation);
        }
        catch (RestClientException exception) {
            log.error("Google Translate REST API error", exception);
            return product;
        }
    }

    private void logCurrentProgress(AtomicInteger counter, int productsCount) {
        Float progress = 100 * counter.addAndGet(1) / (float) productsCount;
        log.info("Product translation progress: " + progress + " %");
    }

    private URI buildUri(String productName) {
        return UriComponentsBuilder.fromUriString(TRANSLATE_URL)
                .queryParam("source", "en")
                .queryParam("target", "pl")
                .queryParam("key", GOOGLE_AUTH)
                .queryParam("q", productName)
                .build()
                .toUri();
    }
}
