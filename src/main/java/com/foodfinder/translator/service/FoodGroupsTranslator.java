package com.foodfinder.translator.service;

import com.foodfinder.food.dao.FoodGroupRepository;
import com.foodfinder.food.domain.entity.FoodGroup;
import com.foodfinder.translator.domain.dto.TranslateResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
class FoodGroupsTranslator {

    private final FoodGroupRepository foodGroupRepository;

    @Value("${google.translate.url}")
    private String TRANSLATE_URL;

    @Value("${google.translate.auth}")
    private String GOOGLE_AUTH;

    void translate() {
        List<FoodGroup> foodGroups = foodGroupRepository.findAll()
                .stream()
                .map(this::translateSingleGroup)
                .collect(Collectors.toList());

        foodGroupRepository.save(foodGroups);
    }

    private FoodGroup translateSingleGroup(FoodGroup foodGroup) {
        try {
            String translation = new RestTemplate().getForObject(buildUri(foodGroup.getName()), TranslateResponseDTO.class)
                    .getData()
                    .getTranslations()
                    .get(0)
                    .getTranslatedText();

            return foodGroup.setName(translation);
        }
        catch (RestClientException exception) {
            log.error("Google Translate REST API error", exception);
            return foodGroup;
        }
    }

    private URI buildUri(String groupName) {
        return UriComponentsBuilder.fromUriString(TRANSLATE_URL)
                .queryParam("source", "en")
                .queryParam("target", "pl")
                .queryParam("key", GOOGLE_AUTH)
                .queryParam("q", groupName)
                .build()
                .toUri();
    }
}
