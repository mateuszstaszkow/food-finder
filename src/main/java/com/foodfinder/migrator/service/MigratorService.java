package com.foodfinder.migrator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Transactional
public class MigratorService {

    private final FoodGroupsMigrator foodGroupsMigrator;
    private final ProductsMigrator productsMigrator;

    public void migrateFoodGroups() {
        foodGroupsMigrator.migrate();
    }

    public void migrateProducts() {
        productsMigrator.migrate(foodGroupsMigrator.getFoodGroupsResponse());
    }

    public void migrateAll() {
        migrateFoodGroups();
        migrateProducts();
    }

}
