ALTER TABLE `user` DROP FOREIGN KEY `fk_user_pal`;
ALTER TABLE `user` DROP INDEX `fk_user_pal`;

UPDATE `pal` SET name='low' WHERE name='LOW';
UPDATE `pal` SET name='medium' WHERE name='MEDIUM';
UPDATE `pal` SET name='high' WHERE name='HIGH';

ALTER TABLE `user` ADD CONSTRAINT `fk_user_pal` FOREIGN KEY (`pal`) REFERENCES `pal` (`name`);