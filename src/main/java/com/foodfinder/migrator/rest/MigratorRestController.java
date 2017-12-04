package com.foodfinder.migrator.rest;

import com.foodfinder.migrator.MigratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}")
public class MigratorRestController {

    private final MigratorService migratorService;

    @RequestMapping(value = "/migrate/products", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void migrateProducts() {
        migratorService.migrateProducts();
    }

    @RequestMapping(value = "/migrate/groups", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void migrateFoodGroups() {
        migratorService.migrateFoodGroups();
    }
}
