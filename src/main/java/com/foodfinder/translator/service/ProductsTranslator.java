package com.foodfinder.translator.service;

import com.foodfinder.food.repository.ProductRepository;
import com.foodfinder.food.domain.entity.Product;
import com.foodfinder.translator.domain.dto.TranslateResponseDTO;
import com.google.common.math.IntMath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.RoundingMode;
import java.net.URI;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@PropertySource("/secrets.properties")
class ProductsTranslator implements Translator {

    private final ProductRepository productRepository;

    private static final int PAGE_SIZE = 1500;

    @Value("${google.translate.url}")
    private String TRANSLATE_URL;

    @Value("${google.translate.auth}")
    private String GOOGLE_AUTH;

    @Override
    public void translate() {
        Long start = new Date().getTime();
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

        logStatus(start);
    }

    private void logStatus(Long start) {
        Float duration = (new Date().getTime() - start) / (float) 1000;
        log.info("Products count:" + productRepository.count() + ", time: " + duration + " s");
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
            product.setTranslatedName(translation);
            return product;
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
