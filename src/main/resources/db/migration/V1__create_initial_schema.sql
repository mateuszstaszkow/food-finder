CREATE TABLE `composition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gm` float DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `unit` varchar(100) DEFAULT NULL,
  `value` float DEFAULT NULL,
  `name_pl` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33953 DEFAULT CHARSET=utf8;

CREATE TABLE `diagnostic` (
  `id` bigint(20) NOT NULL,
  `exception` varchar(1000) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL,
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
  `name` varchar(50) DEFAULT NULL,
  `hits` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_r7g2l08wdh3uv3gvurli4s1bx` (`name`)
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
  `name` varchar(200) NOT NULL,
  `short_description` varchar(100) DEFAULT NULL,
  `weight` float DEFAULT NULL,
  `foodGroup_id` bigint(20) DEFAULT NULL,
  `hits` bigint(20) DEFAULT NULL,
  `name_pl` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jmivyxk9rmgysrmsqw15lqr5b` (`name`),
  KEY `FK9umas66vc9pksourymbq1xv0b` (`foodGroup_id`),
  CONSTRAINT `FK9umas66vc9pksourymbq1xv0b` FOREIGN KEY (`foodGroup_id`) REFERENCES `food_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8489 DEFAULT CHARSET=utf8;

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `surname` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
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
  UNIQUE KEY `UK_4mfaw8qfcne80y2l418iafvdf` (`products_id`),
  KEY `FKfxless6waa8gn5kkmxmij8hyj` (`dish_id`),
  CONSTRAINT `FK9kkqi43b5inyac9ny56c61osp` FOREIGN KEY (`products_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKfxless6waa8gn5kkmxmij8hyj` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`)
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
  UNIQUE KEY `UK_mitmgq1leeohfmebclq5lgr5g` (`privileges_id`),
  KEY `FKsykrtrdngu5iexmbti7lu9xa` (`role_id`),
  CONSTRAINT `FKqq5610tm0bc0iyfl6qgaavpu9` FOREIGN KEY (`privileges_id`) REFERENCES `privilege` (`id`),
  CONSTRAINT `FKsykrtrdngu5iexmbti7lu9xa` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `role_user` (
  `role_id` bigint(20) NOT NULL,
  `users_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK_kfajhki6vd9okapq5eov6tk9c` (`users_id`),
  KEY `FKiqpmjd2qb4rdkej916ymonic6` (`role_id`),
  CONSTRAINT `FKiqpmjd2qb4rdkej916ymonic6` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FKldxwe0761b1sqf4k3waq97j0f` FOREIGN KEY (`users_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;