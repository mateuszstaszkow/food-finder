ALTER TABLE `product`
    CHANGE COLUMN `name` `name` VARCHAR(200) NULL,
    CHANGE COLUMN `hits` `hits` BIGINT(20) ZEROFILL UNSIGNED NULL DEFAULT 0,
    DROP INDEX `UK_jmivyxk9rmgysrmsqw15lqr5b`;

