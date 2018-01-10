ALTER TABLE `product` CHANGE COLUMN `hits` `hits` BIGINT(20) NULL DEFAULT 0;

UPDATE `product` SET hits = 0;