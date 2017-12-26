package com.foodfinder.food.domain.mapper;

import com.foodfinder.food.domain.dto.CompositionDTO;
import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.food.domain.entity.Composition;
import com.foodfinder.food.domain.entity.FoodGroup;
import com.foodfinder.food.domain.entity.Product;
import com.foodfinder.food.service.FoodTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodMapper {
    
    private final FoodTranslationService foodTranslationService;

    public Composition toEntity(CompositionDTO compositionDTO) {
        if ( compositionDTO == null ) {
            return null;
        }

        Composition composition = new Composition();

        composition.setId( compositionDTO.getId() );
        composition.setUnit( compositionDTO.getUnit() );
        composition.setValue( compositionDTO.getValue() );
        if(foodTranslationService.isPolishLanguage()) {
            composition.setTranslatedName( compositionDTO.getName() );
        } else {
            composition.setName( compositionDTO.getName() );
        }

        return composition;
    }

    public CompositionDTO toDto(Composition composition) {
        if ( composition == null ) {
            return null;
        }

        CompositionDTO compositionDTO = new CompositionDTO();

        compositionDTO.setId( composition.getId() );
        compositionDTO.setUnit( composition.getUnit() );
        compositionDTO.setValue( composition.getValue() );
        if(foodTranslationService.isPolishLanguage()) {
            compositionDTO.setName( composition.getTranslatedName() );
        } else {
            compositionDTO.setName( composition.getName() );
        }

        return compositionDTO;
    }

    public Product toEntity(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        Product product = new Product();

        product.setFoodGroup( toEntity( productDTO.getFoodGroup() ) );
        product.setId( productDTO.getId() );
        product.setWeight( productDTO.getWeight() );
        product.setMeasure( productDTO.getMeasure() );
        product.setComposition( compositionDTOListToCompositionList( productDTO.getComposition() ) );
        product.setHits( productDTO.getHits() );
        if(foodTranslationService.isPolishLanguage()) {
            product.setTranslatedName( productDTO.getName() );
        } else {
            product.setName( productDTO.getName() );
        }

        return product;
    }

    public ProductDTO toDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setId( product.getId() );
        productDTO.setWeight( product.getWeight() );
        productDTO.setMeasure( product.getMeasure() );
        productDTO.setComposition( compositionListToDto( product.getComposition() ) );
        productDTO.setFoodGroup( toDto( product.getFoodGroup() ) );
        productDTO.setHits( product.getHits() );
        if(foodTranslationService.isPolishLanguage()) {
            productDTO.setName( product.getTranslatedName() );
        } else {
            productDTO.setName( product.getName() );
        }

        return productDTO;
    }

    public List<ProductDTO> productListToDto(Page<Product> productPage) {
        if ( productPage == null ) {
            return null;
        }

        List<ProductDTO> list = new ArrayList<>();
        for ( Product product : productPage ) {
            list.add( toDto( product ) );
        }

        return list;
    }

    public List<ProductDTO> productListToDto(List<Product> productList) {
        if ( productList == null ) {
            return null;
        }

        List<ProductDTO> list = new ArrayList<>(productList.size());
        for ( Product product : productList ) {
            list.add( toDto( product ) );
        }

        return list;
    }

    public FoodGroupDTO toDto(FoodGroup foodGroup) {
        if ( foodGroup == null ) {
            return null;
        }

        FoodGroupDTO foodGroupDTO = new FoodGroupDTO();

        foodGroupDTO.setId( foodGroup.getId() );
        if(foodTranslationService.isPolishLanguage()) {
            foodGroupDTO.setName( foodGroup.getTranslatedName() );
        } else {
            foodGroupDTO.setName( foodGroup.getName() );
        }

        return foodGroupDTO;
    }

    public FoodGroup toEntity(FoodGroupDTO foodGroupDTO) {
        if ( foodGroupDTO == null ) {
            return null;
        }

        FoodGroup foodGroup = new FoodGroup();

        foodGroup.setId( foodGroupDTO.getId() );
        if(foodTranslationService.isPolishLanguage()) {
            foodGroup.setTranslatedName( foodGroupDTO.getName() );
        } else {
            foodGroup.setName( foodGroupDTO.getName() );
        }

        return foodGroup;
    }

    public List<FoodGroupDTO> foodGroupListToDto(Page<FoodGroup> foodGroupPage) {
        if ( foodGroupPage == null ) {
            return null;
        }

        List<FoodGroupDTO> list = new ArrayList<>();
        for ( FoodGroup foodGroup : foodGroupPage ) {
            list.add( toDto( foodGroup ) );
        }

        return list;
    }

    public List<FoodGroupDTO> foodGroupListToDto(List<FoodGroup> foodGroupList) {
        if ( foodGroupList == null ) {
            return null;
        }

        List<FoodGroupDTO> list = new ArrayList<>(foodGroupList.size());
        for ( FoodGroup foodGroup : foodGroupList ) {
            list.add( toDto( foodGroup ) );
        }

        return list;
    }

    public List<CompositionDTO> compositionListToDto(List<Composition> compositionList) {
        if ( compositionList == null ) {
            return null;
        }

        List<CompositionDTO> list = new ArrayList<>(compositionList.size());
        for ( Composition composition : compositionList ) {
            list.add( toDto( composition ) );
        }

        return list;
    }

    public List<Composition> compositionDTOListToCompositionList(List<CompositionDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Composition> list1 = new ArrayList<>(list.size());
        for ( CompositionDTO compositionDTO : list ) {
            list1.add( toEntity( compositionDTO ) );
        }

        return list1;
    }
}
