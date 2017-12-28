ALTER TABLE `dish_product`
    DROP FOREIGN KEY `FK9kkqi43b5inyac9ny56c61osp`,
    DROP INDEX `UK_4mfaw8qfcne80y2l418iafvdf`,
    ADD CONSTRAINT `fk_dish_product_dish` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`),
    ADD CONSTRAINT `uk_dish_product_dish` UNIQUE (`dish_id`);