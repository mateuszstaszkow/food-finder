ALTER TABLE `dish_product`
    DROP FOREIGN KEY `fk_dish_product_dish`,
    ADD CONSTRAINT `fk_dish_product_product` FOREIGN KEY (`products_id`) REFERENCES `product` (`id`);