package com.foodfinder.food.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodTranslationService {

    private final HttpServletRequest request;

    @Value("${food-finder.polish-flag}")
    private String polishFlag;

    public Boolean isPolishLanguage() {
        return getCurrentCountry().equals(polishFlag);
    }

    public String getCurrentCountry() {
        return new AcceptHeaderLocaleResolver().resolveLocale(request)
                .getCountry();
    }
}
