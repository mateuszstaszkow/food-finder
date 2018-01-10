ALTER TABLE `diagnostic` MODIFY COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `diet_user`
    DROP FOREIGN KEY `FKn6k23uki3tqxidgfntuwf1t7g`,
    DROP FOREIGN KEY `FK5ty89byma1wy79scdgfcr2p1e`;
ALTER TABLE `diet_user`
    DROP INDEX `FK5ty89byma1wy79scdgfcr2p1e` ,
    DROP INDEX `FKn6k23uki3tqxidgfntuwf1t7g` ;

ALTER TABLE `user`
    DROP FOREIGN KEY `fk_user_role`;
ALTER TABLE `user`
    DROP INDEX `fk_user_role` ;

ALTER TABLE `role_privilege`
    DROP FOREIGN KEY `FKsykrtrdngu5iexmbti7lu9xa`,
    DROP FOREIGN KEY `FKqq5610tm0bc0iyfl6qgaavpu9`;
ALTER TABLE `role_privilege`
    DROP INDEX `FKqq5610tm0bc0iyfl6qgaavpu9` ,
    DROP INDEX `FKsykrtrdngu5iexmbti7lu9xa` ;

ALTER TABLE `user_day`
    DROP FOREIGN KEY `fk_user_day_user`;
ALTER TABLE `user_day`
    DROP INDEX `fk_user_day` ;

ALTER TABLE `diet` MODIFY COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE `privilege` MODIFY COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE `role` MODIFY COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE `user` MODIFY COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `diet_user`
    ADD CONSTRAINT `FK5ty89byma1wy79scdgfcr2p1e` FOREIGN KEY (`diets_id`) REFERENCES `diet` (`id`),
    ADD CONSTRAINT `FKn6k23uki3tqxidgfntuwf1t7g` FOREIGN KEY (`users_id`) REFERENCES `user` (`id`);
    
ALTER TABLE `user`
    ADD CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);
    
ALTER TABLE `role_privilege`
    ADD CONSTRAINT `FKqq5610tm0bc0iyfl6qgaavpu9` FOREIGN KEY (`privileges_id`) REFERENCES `privilege` (`id`),
    ADD CONSTRAINT `FKsykrtrdngu5iexmbti7lu9xa` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);
    
    ALTER TABLE `user_day`
    ADD CONSTRAINT `fk_user_day_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);