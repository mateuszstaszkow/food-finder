CREATE TABLE `day` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `date` DATETIME(0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `timed_dish` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` DATETIME(0) DEFAULT NULL,
  `dish_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_timed_dish_dish` (`dish_id`),
  CONSTRAINT `fk_timed_dish_dish` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `day_timed_dish` (
  `day_id` bigint(20) NOT NULL,
  `timed_dish_id` bigint(20) NOT NULL,
  KEY `fk_day_timed_dish` (`day_id`),
  CONSTRAINT `fk_day_timed_dish_timed_dish` FOREIGN KEY (`timed_dish_id`) REFERENCES `timed_dish` (`id`),
  CONSTRAINT `fk_day_timed_dish_day` FOREIGN KEY (`day_id`) REFERENCES `day` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;