package com.foodfinder.food.domain.mapper;

import com.foodfinder.food.domain.dto.CompositionDTO;
import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.food.domain.entity.Composition;
import com.foodfinder.food.domain.entity.FoodGroup;
import com.foodfinder.food.domain.entity.Product;
import com.foodfinder.food.repository.CompositionRepository;
import com.foodfinder.food.repository.FoodGroupRepository;
import com.foodfinder.food.repository.ProductRepository;
import com.foodfinder.food.service.FoodTranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FoodMapper {
    
    private final FoodTranslationService foodTranslationService;
    private final ProductRepository productRepository;
    private final FoodGroupRepository foodGroupRepository;
    private final CompositionRepository compositionRepository;

    public Composition toEntity(CompositionDTO compositionDTO) {
        if ( compositionDTO == null ) {
            return null;
        }

        Composition composition = new Composition();
        if(compositionDTO.getId() != null) {
            composition = Optional.ofNullable(compositionRepository.findOne(compositionDTO.getId()))
                    .orElse(new Composition());
        }

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
        if(productDTO.getId() != null) {
            product = Optional.ofNullable(productRepository.findById(productDTO.getId()))
                    .orElse(new Product());
        }

        product.setFoodGroup( toEntity( productDTO.getFoodGroup() ) );
        product.setId( productDTO.getId() );
        product.setWeight( productDTO.getWeight() );
        product.setMeasure( productDTO.getMeasure() );
        product.setComposition( compositionDTOListToCompositionSet( productDTO.getComposition() ) );
        product.setProtein( toEntity(productDTO.getProtein()) );
        product.setCarbohydrates( toEntity(productDTO.getCarbohydrates()) );
        product.setFat( toEntity(productDTO.getFat()) );
        product.setEnergy( toEntity(productDTO.getEnergy()) );
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
        productDTO.setComposition( compositionSetToDto( product.getComposition() ) );
        productDTO.setFoodGroup( toDto( product.getFoodGroup() ) );
        productDTO.setProtein( toDto(product.getProtein()) );
        productDTO.setCarbohydrates( toDto(product.getCarbohydrates()) );
        productDTO.setFat( toDto(product.getFat()) );
        productDTO.setEnergy( toDto(product.getEnergy()) );
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

    public List<ProductDTO> productListToDto(Set<Product> productList) {
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
        if(foodGroupDTO.getId() != null) {
            foodGroup = Optional.ofNullable(foodGroupRepository.findOne(foodGroupDTO.getId()))
                    .orElse(new FoodGroup());
        }

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

    private List<CompositionDTO> compositionSetToDto(Set<Composition> compositionList) {
        if ( compositionList == null ) {
            return null;
        }

        List<CompositionDTO> list = new ArrayList<>(compositionList.size());
        for ( Composition composition : compositionList ) {
            list.add( toDto( composition ) );
        }

        return list;
    }

    private Set<Composition> compositionDTOListToCompositionSet(List<CompositionDTO> list) {
        if ( list == null ) {
            return null;
        }

        Set<Composition> dbList = new HashSet<>(list.size());
        for ( CompositionDTO compositionDTO : list ) {
            dbList.add( toEntity( compositionDTO ) );
        }

        return dbList;
    }

    public List<Product> productListToEntity(List<ProductDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Product> dbList = new ArrayList<>( list.size() );
        for ( ProductDTO productDTO : list ) {
            dbList.add( toEntity( productDTO ) );
        }

        return dbList;
    }

    public Set<Product> productListToEntitySet(List<ProductDTO> list) {
        if ( list == null ) {
            return null;
        }

        Set<Product> dbSet = new HashSet<>( list.size() );
        for ( ProductDTO productDTO : list ) {
            dbSet.add( toEntity( productDTO ) );
        }

        return dbSet;
    }
}
