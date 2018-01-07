CREATE TABLE `composition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gm` float DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `unit` varchar(100) DEFAULT NULL,
  `value` float DEFAULT NULL,
  `name_pl` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33965 DEFAULT CHARSET=utf8;

CREATE TABLE `day` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8;

CREATE TABLE `diagnostic` (
  `id` bigint(20) NOT NULL,
  `exception` varchar(1000) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL,
  `comments` varchar(1000) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `diet` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `dish` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(2000) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `hits` bigint(20) DEFAULT NULL,
  `name_pl` varchar(200) DEFAULT NULL,
  `description_pl` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8;

CREATE TABLE `flyway_schema_history` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `food_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `name_pl` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_a935bmmnn6yvyc46tv5rkwcyq` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `privilege` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(1000) DEFAULT NULL,
  `measure` varchar(100) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `short_description` varchar(100) DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `foodGroup_id` bigint(20) DEFAULT NULL,
  `hits` bigint(20) DEFAULT '0',
  `name_pl` varchar(200) DEFAULT NULL,
  `protein` bigint(20) DEFAULT NULL,
  `carbohydrates` bigint(20) DEFAULT NULL,
  `fat` bigint(20) DEFAULT NULL,
  `energy` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9umas66vc9pksourymbq1xv0b` (`foodGroup_id`),
  KEY `fk_product_protein` (`protein`),
  KEY `fk_product_carbohydrates` (`carbohydrates`),
  KEY `fk_product_fat` (`fat`),
  KEY `fk_product_energy` (`energy`),
  CONSTRAINT `FK9umas66vc9pksourymbq1xv0b` FOREIGN KEY (`foodGroup_id`) REFERENCES `food_group` (`id`),
  CONSTRAINT `fk_product_carbohydrates` FOREIGN KEY (`carbohydrates`) REFERENCES `composition` (`id`),
  CONSTRAINT `fk_product_energy` FOREIGN KEY (`energy`) REFERENCES `composition` (`id`),
  CONSTRAINT `fk_product_fat` FOREIGN KEY (`fat`) REFERENCES `composition` (`id`),
  CONSTRAINT `fk_product_protein` FOREIGN KEY (`protein`) REFERENCES `composition` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8489 DEFAULT CHARSET=utf8;

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `timed_dish` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `dish_id` bigint(20) DEFAULT NULL,
  `dish_order` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_timed_dish_dish` (`dish_id`),
  CONSTRAINT `fk_timed_dish_dish` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `surname` varchar(50) DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `height` float DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `gender` varchar(50) DEFAULT NULL,
  `role_id` bigint(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
  KEY `fk_user_role` (`role_id`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_day` (
  `user_id` bigint(20) NOT NULL,
  `day_id` bigint(20) NOT NULL,
  KEY `fk_user_day` (`user_id`),
  KEY `fk_user_day_day` (`day_id`),
  CONSTRAINT `fk_user_day_day` FOREIGN KEY (`day_id`) REFERENCES `day` (`id`),
  CONSTRAINT `fk_user_day_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `day_timed_dish` (
  `day_id` bigint(20) NOT NULL,
  `timed_dish_id` bigint(20) NOT NULL,
  KEY `fk_day_timed_dish` (`day_id`),
  KEY `fk_day_timed_dish_timed_dish` (`timed_dish_id`),
  CONSTRAINT `fk_day_timed_dish_day` FOREIGN KEY (`day_id`) REFERENCES `day` (`id`),
  CONSTRAINT `fk_day_timed_dish_timed_dish` FOREIGN KEY (`timed_dish_id`) REFERENCES `timed_dish` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `diet_user` (
  `diets_id` bigint(20) NOT NULL,
  `users_id` bigint(20) NOT NULL,
  KEY `FKn6k23uki3tqxidgfntuwf1t7g` (`users_id`),
  KEY `FK5ty89byma1wy79scdgfcr2p1e` (`diets_id`),
  CONSTRAINT `FK5ty89byma1wy79scdgfcr2p1e` FOREIGN KEY (`diets_id`) REFERENCES `diet` (`id`),
  CONSTRAINT `FKn6k23uki3tqxidgfntuwf1t7g` FOREIGN KEY (`users_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `dish_product` (
  `dish_id` bigint(20) NOT NULL,
  `products_id` bigint(20) NOT NULL,
  KEY `fk_dish_product_index` (`products_id`),
  KEY `fk_dish_product_dish` (`dish_id`),
  CONSTRAINT `fk_dish_product_dish` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`),
  CONSTRAINT `fk_dish_product_product` FOREIGN KEY (`products_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `product_composition` (
  `product_id` bigint(20) NOT NULL,
  `composition_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_ck9brrodoec2vrggadmhs0pgd` (`composition_id`),
  KEY `FKt65hx96eh63gbelkc8dpyc1jq` (`product_id`),
  CONSTRAINT `FKsgqsn6y67ikf6vocco5q1jdyp` FOREIGN KEY (`composition_id`) REFERENCES `composition` (`id`),
  CONSTRAINT `FKt65hx96eh63gbelkc8dpyc1jq` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role_privilege` (
  `role_id` bigint(20) NOT NULL,
  `privileges_id` bigint(20) NOT NULL,
  KEY `FKsykrtrdngu5iexmbti7lu9xa` (`role_id`),
  KEY `FKqq5610tm0bc0iyfl6qgaavpu9` (`privileges_id`),
  CONSTRAINT `FKqq5610tm0bc0iyfl6qgaavpu9` FOREIGN KEY (`privileges_id`) REFERENCES `privilege` (`id`),
  CONSTRAINT `FKsykrtrdngu5iexmbti7lu9xa` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;