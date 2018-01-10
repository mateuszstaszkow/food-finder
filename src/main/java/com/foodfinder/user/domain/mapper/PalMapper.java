package com.foodfinder.user.domain.mapper;

import com.foodfinder.user.domain.entity.Pal;
import com.foodfinder.user.repository.PalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PalMapper {

    private final PalRepository palRepository;

    public String toDto(Pal pal) {
        return Optional.ofNullable(pal)
                .map(Pal::getName)
                .orElse(null);
    }

    public Pal toEntity(String pal) {
        return Optional.ofNullable(pal)
                .map(palRepository::findOne)
                .orElse(null);
    }
}
