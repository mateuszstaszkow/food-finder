package com.foodfinder.migrator;

import com.foodfinder.migrator.dto.FoodGroupResponseDTO;
import com.foodfinder.migrator.dto.UsdaGroupResponseDTO;
import com.foodfinder.exceptions.UsdaRestException;
import com.foodfinder.food.domain.FoodGroup;
import com.foodfinder.food.domain.FoodGroupRepository;
import com.foodfinder.food.domain.FoodMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional
class FoodGroupsMigrator {

    private final FoodMapper foodMapper;
    private final FoodGroupRepository foodGroupRepository;

    @Value("${usda.groups.url}")
    private String GROUPS_URL;

    @Value("${usda.auth}")
    private String USDA_AUTH;

    void migrate() {
        Long start = new Date().getTime();
        migrateAllFoodGroups();
        logStatus(start);
    }

    List<FoodGroupResponseDTO> getFoodGroupsResponse() {
        try {
            return new RestTemplate().getForObject(buildUri(), UsdaGroupResponseDTO.class)
                    .getList()
                    .getItem();
        }
        catch (RestClientException exception) {
            log.error("USDA REST API error (food groups)", exception);
            throw new UsdaRestException("USDA REST API error (food groups)", exception);
        }
    }

    private void logStatus(Long start) {
        Float duration = (new Date().getTime() - start) / (float) 1000;
        log.info("Groups count:" + foodGroupRepository.count() + ", time: " + duration + " s");
    }

    private void migrateAllFoodGroups() {
        try {
            List<FoodGroup> groupList = new RestTemplate().getForObject(buildUri(), UsdaGroupResponseDTO.class)
                    .getList()
                    .getItem()
                    .stream()
                    .filter(groupDto -> foodGroupRepository.findByDescription(groupDto.getName()) == null)
                    .map(foodMapper::toEntity)
                    .collect(Collectors.toList());

            foodGroupRepository.save(groupList);
        }
        catch (RestClientException exception) {
            log.error("USDA REST API error", exception);
        }
    }

    private URI buildUri() {
        return UriComponentsBuilder.fromUriString(GROUPS_URL)
                .queryParam("format", "json")
                .queryParam("api_key", USDA_AUTH)
                .queryParam("sort", "id")
                .queryParam("lt", "g")
                .build()
                .toUri();
    }
}
