ALTER TABLE `dish_product`
    DROP FOREIGN KEY `FKfxless6waa8gn5kkmxmij8hyj`,
    DROP INDEX `uk_dish_product_dish`,
    ADD CONSTRAINT `fk_dish_product_dish` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`);