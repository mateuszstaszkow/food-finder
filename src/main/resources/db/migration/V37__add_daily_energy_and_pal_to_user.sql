CREATE TABLE `pal` (
  `name` VARCHAR(50) NOT NULL,
  `value` FLOAT NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `user`
    ADD daily_energy INT(11) NULL,
    ADD pal VARCHAR(50) NULL,
    ADD CONSTRAINT `fk_user_pal` FOREIGN KEY (`pal`) REFERENCES `pal` (`name`);

INSERT INTO `pal` VALUES ('LOW', 1.4);
INSERT INTO `pal` VALUES ('MEDIUM', 1.7);
INSERT INTO `pal` VALUES ('HIGH', 2.0);