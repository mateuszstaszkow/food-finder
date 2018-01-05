package com.foodfinder.migrator.domain.mapper;

import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.food.domain.entity.Composition;
import com.foodfinder.food.domain.entity.FoodGroup;
import com.foodfinder.food.domain.entity.Product;
import com.foodfinder.migrator.domain.dto.CompositionResponseDTO;
import com.foodfinder.migrator.domain.dto.FoodGroupResponseDTO;
import com.foodfinder.migrator.domain.dto.ProductResponseDTO;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MigrationMapper {

    private static final String PROTEIN = "Protein";
    private static final String CARBOHYDRATES = "Carbohydrate, by difference";
    private static final String FAT = "Total lipid (fat)";
    private static final String ENERGY = "Energy";

    public FoodGroup toEntity(FoodGroupResponseDTO response) {
        if ( response == null ) {
            return null;
        }

        FoodGroup foodGroup = new FoodGroup();

        if ( response.getId() != null ) {
            foodGroup.setId( Long.parseLong( response.getId() ) );
        }
        foodGroup.setName( response.getName() );

        return foodGroup;
    }

    public Composition toEntity(CompositionResponseDTO response) {
        if ( response == null ) {
            return null;
        }

        Composition composition = new Composition();

        composition.setName( response.getNutrient() );
        composition.setId( response.getNutrient_id() );
        composition.setUnit( response.getUnit() );
        composition.setValue( response.getValue() );
        composition.setGm( response.getGm() );

        return composition;
    }

    public Product toEntity(ProductResponseDTO response) {
        if ( response == null ) {
            return null;
        }
        Product product = new Product();
        List<CompositionResponseDTO> nutrients = getFilteredNutrients(response.getNutrients());

        product.setId( response.getNdbno() );
        product.setProtein( getCompositionFromList(PROTEIN, response.getNutrients()) );
        product.setCarbohydrates( getCompositionFromList(CARBOHYDRATES, response.getNutrients()) );
        product.setFat( getCompositionFromList(FAT, response.getNutrients()) );
        product.setEnergy( getCompositionFromList(ENERGY, response.getNutrients()) );
        product.setComposition( compositionResponseDTOListToCompositionSet( nutrients ) );
        product.setFoodGroup( foodGroupDTOToFoodGroup( response.getFoodGroup() ) );
        product.setName( response.getName() );
        product.setWeight( response.getWeight() );
        product.setMeasure( response.getMeasure() );
        product.setDescription( response.getDescription() );
        product.setShortDescription( response.getShortDescription() );

        return product;
    }

    private Set<Composition> compositionResponseDTOListToCompositionSet(List<CompositionResponseDTO> list) {
        if ( list == null ) {
            return null;
        }

        Set<Composition> set = new HashSet<>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( CompositionResponseDTO compositionResponseDTO : list ) {
            set.add( toEntity( compositionResponseDTO ) );
        }

        return set;
    }

    private FoodGroup foodGroupDTOToFoodGroup(FoodGroupDTO foodGroupDTO) {
        if ( foodGroupDTO == null ) {
            return null;
        }

        FoodGroup foodGroup = new FoodGroup();

        foodGroup.setId( foodGroupDTO.getId() );
        foodGroup.setName( foodGroupDTO.getName() );

        return foodGroup;
    }

    private Composition getCompositionFromList(String name, List<CompositionResponseDTO> nutrients) {
        return nutrients.stream()
                .filter(nutrient -> nutrient.getNutrient().equals(name))
                .map(this::toEntity)
                .collect(Collectors.toList())
                .get(0);
    }

    private List<CompositionResponseDTO> getFilteredNutrients(List<CompositionResponseDTO> nutrients) {
        return nutrients.stream()
                .filter(this::isNotPrimaryComposition)
                .collect(Collectors.toList());
    }

    private Boolean isNotPrimaryComposition(CompositionResponseDTO nutrient) {
        Boolean notProtein = !nutrient.getNutrient().equals(PROTEIN);
        Boolean notCarbohydrates = !nutrient.getNutrient().equals(CARBOHYDRATES);
        Boolean notFat = !nutrient.getNutrient().equals(FAT);
        Boolean notEnergy = !nutrient.getNutrient().equals(ENERGY);

        return notProtein && notCarbohydrates && notFat && notEnergy;
    }
}