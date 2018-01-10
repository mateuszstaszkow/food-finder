ALTER TABLE `product`
    ADD protein bigint(20) NULL,
    ADD carbohydrates bigint(20) NULL,
    ADD fat bigint(20) NULL,
    ADD energy bigint(20) NULL,
    ADD CONSTRAINT `fk_product_protein` FOREIGN KEY (`protein`) REFERENCES `composition` (`id`),
    ADD CONSTRAINT `fk_product_carbohydrates` FOREIGN KEY (`carbohydrates`) REFERENCES `composition` (`id`),
    ADD CONSTRAINT `fk_product_fat` FOREIGN KEY (`fat`) REFERENCES `composition` (`id`),
    ADD CONSTRAINT `fk_product_energy` FOREIGN KEY (`energy`) REFERENCES `composition` (`id`);