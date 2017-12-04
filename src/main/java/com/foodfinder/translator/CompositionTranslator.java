package com.foodfinder.translator;

import com.foodfinder.food.domain.Composition;
import com.foodfinder.food.domain.CompositionRepository;
import com.google.common.math.IntMath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional
class CompositionTranslator {

    private final CompositionRepository compositionRepository;

    private static final int PAGE_SIZE = 1500;

    void translate() {
        AtomicInteger counter = new AtomicInteger(0);
        int compositionCount = (int) compositionRepository.count();
        int numberOfPages = IntMath.divide(compositionCount, PAGE_SIZE, RoundingMode.CEILING);

        IntStream.range(0, numberOfPages)
                .forEach(page -> {
                    Page<Composition> compositionPage = compositionRepository.findAll(new PageRequest(page, PAGE_SIZE))
                            .map(composition -> translateSingleComposition(composition, counter, compositionCount));

                    compositionRepository.save(compositionPage);
                });
    }

    private void logCurrentProgress(AtomicInteger counter, int compositionCount) {
        Float progress = 100 * counter.addAndGet(1) / (float) compositionCount;
        log.info("Product translation progress: " + progress + " %");
    }

    private Composition translateSingleComposition(Composition composition, AtomicInteger counter, int compositionCount) {
        String translation = "";

        switch (composition.getNutrient()) {
            case "Energy":
                translation = "Energia";
                break;
            case "Protein":
                translation = "Białko";
                break;
            case "Total lipid (fat)":
                translation = "Tłuszcz";
                break;
            case "Carbohydrate, by difference":
                translation = "Węglowodany";
                break;
        }

        logCurrentProgress(counter, compositionCount);
        return composition.setNutrient(translation);
    }
}
