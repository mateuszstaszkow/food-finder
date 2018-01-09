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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `exception` varchar(4000) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL,
  `comments` varchar(1000) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `diet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

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

CREATE TABLE `pal` (
  `name` VARCHAR(50) NOT NULL,
  `value` FLOAT NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `privilege` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
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
  `daily_energy` INT(11) NULL,
  `pal` VARCHAR(50) NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
  KEY `fk_user_role` (`role_id`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `fk_user_pal` FOREIGN KEY (`pal`) REFERENCES `pal` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

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

INSERT INTO `privilege` VALUES (1,'VIEW_USERS'),(2,'VIEW_ADMIN'),(3,'VIEW_PRODUCTS'),(4,'VIEW_GROUPS'),
(5,'VIEW_DISHES'),(6,'VIEW_DIETS'),(7,'VIEW_DAYS'),(8,'RECOGNIZE'),(9,'MIGRATE'),(10,'TRANSLATE'),(11,'VIEW_ROLES'),
(12,'VIEW_PRIVILEGES'),(13,'VIEW_ACCOUNT');

INSERT INTO `role` VALUES (1,'ADMIN'),(2,'USER');

INSERT INTO `role_privilege` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(2,3),(2,4),(2,5),(2,6),(2,7),(1,8),
(1,9),(1,10),(1,11),(1,12),(1,13),(2,13);

INSERT INTO `user` VALUES (1,'admin@foodfinder.com','','',0,0,0,'',1,
'$2a$10$6RgTDLtXfDgst20Mxt89/Oviq6vuO0QDXLKV.CbqYuapQ7vF6bbOq',1,null,null),(2,'user@foodfinder.com','','',0,0,0,'',2,
'$2a$10$uhBXm6h8VpX0hq.VEdtqle7xLKo2pksdBcaUxgh0Rau2XGQHsO2sm',1,null,null),(3,'janusz@foodfinder.com','Janusz','Testowy',
NULL,NULL,0,NULL,2,'$2a$10$syZyKe95sbglXa4cHmyNBeI0vhEWFahkjqpCb6fnr1ZlJ1WucHTTm',1,null,null);

INSERT INTO `pal` VALUES ('LOW', 1.4);
INSERT INTO `pal` VALUES ('MEDIUM', 1.7);
INSERT INTO `pal` VALUES ('HIGH', 2.0);

INSERT INTO `composition` VALUES (1,66.2,'Carbohydrate, by difference','g',4.9,'Węglowodany'),(2,353,'Energy','kcal',26,
'Energia'),(3,1.4,'Total lipid (fat)','g',0.1,'Tłuszcz'),(4,19.9,'Protein','g',1.47,'Białko'),(5,41,
'Carbohydrate, by difference','g',2.3,'Węglowodany'),(6,358,'Energy','kcal',20,'Energia'),(7,5.1,'Total lipid (fat)','g'
,0.29,'Tłuszcz'),(8,35.8,'Protein','g',2,'Białko'),(9,52.9,'Carbohydrate, by difference','g',3.7,'Węglowodany'),(10,329,
'Energy','kcal',23,'Energia'),(11,2.6,'Total lipid (fat)','g',0.18,'Tłuszcz'),(12,23.5,'Protein','g',1.65,'Białko'),(13,
0,'Carbohydrate, by difference','g',0,'Węglowodany'),(14,876,'Energy','kcal',112,'Energia'),(15,99.48,
'Total lipid (fat)','g',12.73,'Tłuszcz'),(16,0.28,'Protein','g',0.04,'Białko'),(17,0.06,'Carbohydrate, by difference',
'g',0,'Węglowodany'),(18,717,'Energy','kcal',36,'Energia'),(19,81.11,'Total lipid (fat)','g',4.06,'Tłuszcz'),(20,0.85,
'Protein','g',0.04,'Białko'),(21,2.87,'Carbohydrate, by difference','g',0.11,'Węglowodany'),(22,718,'Energy','kcal',27,
'Energia'),(23,78.3,'Total lipid (fat)','g',2.98,'Tłuszcz'),(24,0.49,'Protein','g',0.02,'Białko'),(25,0.06,
'Carbohydrate, by difference','g',0,'Węglowodany'),(26,717,'Energy','kcal',36,'Energia'),(27,81.11,'Total lipid (fat)',
'g',4.06,'Tłuszcz'),(28,0.85,'Protein','g',0.04,'Białko'),(29,8.32,'Carbohydrate, by difference','g',2.36,
'Węglowodany'),(30,331,'Energy','kcal',94,'Energia'),(31,24.46,'Total lipid (fat)','g',6.93,'Tłuszcz'),(32,19.66,
'Protein','g',5.57,'Białko'),(33,16.18,'Carbohydrate, by difference','g',4.59,'Węglowodany'),(34,257,'Energy','kcal',73,
'Energia'),(35,19.5,'Total lipid (fat)','g',5.53,'Tłuszcz'),(36,4.08,'Protein','g',1.16,'Białko'),(37,8.56,
'Carbohydrate, by difference','g',9.67,'Węglowodany'),(38,330,'Energy','kcal',373,'Energia'),(39,25.63,
'Total lipid (fat)','g',28.96,'Tłuszcz'),(40,16.86,'Protein','g',19.05,'Białko'),(41,8.56,'Carbohydrate, by difference',
'g',9.67,'Węglowodany'),(42,330,'Energy','kcal',373,'Energia'),(43,25.63,'Total lipid (fat)','g',28.96,'Tłuszcz'),
(44,16.86,'Protein','g',19.05,'Białko'),(45,4.5,'Carbohydrate, by difference','g',1.28,'Węglowodany'),(46,323,'Energy',
'kcal',92,'Energia'),(47,24.14,'Total lipid (fat)','g',6.84,'Tłuszcz'),(48,21.92,'Protein','g',6.21,'Białko'),(49,10.6,
'Carbohydrate, by difference','g',2.23,'Węglowodany'),(50,240,'Energy','kcal',50,'Energia'),(51,14.1,
'Total lipid (fat)','g',2.96,'Tłuszcz'),(52,17.6,'Protein','g',3.7,'Białko'),(53,8.85,'Carbohydrate, by difference',
'g',1.68,'Węglowodany'),(54,307,'Energy','kcal',58,'Energia'),(55,23.06,'Total lipid (fat)','g',4.38,'Tłuszcz'),(56,
16.09,'Protein','g',3.06,'Białko'),(57,10.6,'Carbohydrate, by difference','g',2.97,'Węglowodany'),(58,240,'Energy',
'kcal',67,'Energia'),(59,14.1,'Total lipid (fat)','g',3.95,'Tłuszcz'),(60,17.6,'Protein','g',4.93,'Białko'),(61,5.48,
'Carbohydrate, by difference','g',1.64,'Węglowodany'),(62,197,'Energy','kcal',59,'Energia'),(63,14.92,
'Total lipid (fat)','g',4.48,'Tłuszcz'),(64,10.33,'Protein','g',3.1,'Białko'),(65,10.71,'Carbohydrate, by difference',
'g',2.25,'Węglowodany'),(66,176,'Energy','kcal',37,'Energia'),(67,8.88,'Total lipid (fat)','g',1.86,'Tłuszcz'),(68,
13.41,'Protein','g',2.82,'Białko'),(69,3.5,'Carbohydrate, by difference','g',0.99,'Węglowodany'),(70,295,'Energy',
'kcal',84,'Energia'),(71,28.6,'Total lipid (fat)','g',8.11,'Tłuszcz'),(72,7.1,'Protein','g',2.01,'Białko'),(73,8.73,
'Carbohydrate, by difference','g',12.22,'Węglowodany'),(74,290,'Energy','kcal',406,'Energia'),(75,21.23,
'Total lipid (fat)','g',29.72,'Tłuszcz'),(76,16.41,'Protein','g',22.97,'Białko'),(77,23.67,
'Carbohydrate, by difference','g',26.75,'Węglowodany'),(78,248,'Energy','kcal',280,'Energia'),(79,12.22,
'Total lipid (fat)','g',13.81,'Tłuszcz'),(80,11.47,'Protein','g',12.96,'Białko'),(81,11.6,'Carbohydrate, by difference',
'g',2.44,'Węglowodany'),(82,239,'Energy','kcal',50,'Energia'),(83,14,'Total lipid (fat)','g',2.94,'Tłuszcz'),(84,16.7,
'Protein','g',3.51,'Białko'),(85,10.53,'Carbohydrate, by difference','g',2,'Węglowodany'),(86,126,'Energy','kcal',24,
'Energia'),(87,0,'Total lipid (fat)','g',0,'Tłuszcz'),(88,21.05,'Protein','g',4,'Białko'),(89,2.34,
'Carbohydrate, by difference','g',0.66,'Węglowodany'),(90,353,'Energy','kcal',100,'Energia'),(91,28.74,
'Total lipid (fat)','g',8.15,'Tłuszcz'),(92,21.4,'Protein','g',6.07,'Białko'),(93,2.79,'Carbohydrate, by difference',
'g',3.68,'Węglowodany'),(94,371,'Energy','kcal',490,'Energia'),(95,29.68,'Total lipid (fat)','g',39.18,'Tłuszcz'),
(96,23.24,'Protein','g',30.68,'Białko'),(97,0.45,'Carbohydrate, by difference','g',0.13,'Węglowodany'),(98,334,'Energy',
'kcal',95,'Energia'),(99,27.68,'Total lipid (fat)','g',7.85,'Tłuszcz'),(100,20.75,'Protein','g',5.88,'Białko');

INSERT INTO `food_group` VALUES (1,'Dairy and Egg Products','Nabiał i produkty jajeczne'),(2,'Spices and Herbs',
'Przyprawy i zioła'),(3,'Baby Foods','Żywność dla dzieci'),(4,'Fats and Oils','Tłuszcze i oleje'),(5,'Poultry Products',
'Produkty drobiowe'),(6,'Soups, Sauces, and Gravies','Zupy, sosy i Gravies'),(7,'Sausages and Luncheon Meats',
'Kiełbaski i lunche mięsne'),(8,'Breakfast Cereals','Płatki śniadaniowe'),(9,'Fruits and Fruit Juices',
'Soki owocowe i owocowe'),(10,'Pork Products','Produkty wieprzowe'),(11,'Vegetables and Vegetable Products',
'Warzywa i produkty warzywne'),(12,'Nut and Seed Products','Produkty z orzechów i nasion'),(13,'Beef Products',
'Produkty z wołowiny'),(14,'Beverages','Napoje'),(15,'Finfish and Shellfish Products','Produkty z ryb i skorupiaków'),
(16,'Legumes and Legume Products','Rośliny strączkowe i produkty roślinne'),(17,'Lamb, Veal, and Game Products',
'Produkty z jagnięciny, cielęciny i gry'),(18,'Baked Products','Pieczone produkty'),(19,'Sweets','Słodycze'),(20,
'Cereal Grains and Pasta','Ziarna zbóż i makaron'),(21,'Fast Foods','Szybkie jedzenie'),(22,
'Meals, Entrees, and Side Dishes','Posiłki, przystawki i przystawki'),(23,'Snacks','Przekąski'),(24,
'American Indian/Alaska Native Foods','American Indian / Alaska Native Foods'),(25,'Restaurant Foods',
'Jedzenie w restauracji');

INSERT INTO `product` VALUES (1,NULL,'1.0 tbsp','Beverage, instant breakfast powder, chocolate, not reconstituted',NULL,
7.4,1,11,'Napój, natychmiastowe śniadanie w proszku, czekolada, nie rekonstytuowany',4,1,3,2),(2,NULL,'1.0 tbsp',
'Beverage, instant breakfast powder, chocolate, sugar-free, not reconstituted',NULL,5.6,1,11,
'Napój, natychmiastowe śniadanie w proszku, czekolada, bez cukru, nierozpuszczony',8,5,7,6),(3,NULL,'1.0 tbsp',
'Beverage, milkshake mix, dry, not chocolate',NULL,7,1,11,'Napój, mieszanka mleczna, sucha, a nie czekolada',12,9,11,
10),(4,NULL,'1.0 tbsp','Butter oil, anhydrous',NULL,12.8,1,0,'Olej maślany, bezwodny',16,13,15,14),(5,NULL,
'1.0 pat (1\" sq, 1/3\" high)','Butter, salted',NULL,5,1,0,'Masło, solone',20,17,19,18),(6,NULL,
'1.0 pat (1\" sq, 1/3\" high)','Butter, whipped, with salt',NULL,3.8,1,0,'Masło ubite solą',24,21,23,22),(7,NULL,
'1.0 pat (1\" sq, 1/3\" high)','Butter, without salt',NULL,5,1,0,'Masło bez soli',28,25,27,26),(8,NULL,'1.0 oz',
'Cheese food, cold pack, American',NULL,28.35,1,0,'Jedzenie z serem, opakowanie na zimno, amerykańskie',32,29,31,30),(9,
NULL,'1.0 oz','Cheese food, pasteurized process, American, imitation, without added vitamin D',NULL,28.35,1,0,
'Żywność serowa, proces pasteryzowany, amerykański, imitacja, bez dodatku witaminy D.',36,33,35,34),(10,NULL,'1.0 cup',
'Cheese food, pasteurized process, American, vitamin D fortified',NULL,113,1,0,
'Żywność serowa, proces pasteryzowany, amerykański, witamina D wzmocniona',40,37,39,38),(11,NULL,'1.0 cup',
'Cheese food, pasteurized process, American, without added vitamin D',NULL,113,1,0,
'Żywność serowa, proces pasteryzowany, amerykański, bez dodatku witaminy D.',44,41,43,42),(12,NULL,'1.0 oz',
'Cheese food, pasteurized process, swiss',NULL,28.35,1,0,'Żywność serowa, proces pasteryzowany, szwajcarski',48,45,47,
46),(13,NULL,'1.0 slice 3/4 oz','Cheese product, pasteurized process, American, reduced fat, fortified with vitamin D',
NULL,21,1,0,
'Produkt serowy, proces pasteryzowany, amerykański, o zmniejszonej zawartości tłuszczu, wzbogacony w witaminę D.',52,49,
51,50),(14,NULL,'1.0 slice (2/3 oz)','Cheese product, pasteurized process, American, vitamin D fortified',NULL,19,1,0,
'Produkt serowy, proces pasteryzowany, amerykański, witamina D wzmocniona',56,53,55,54),(15,NULL,
'1.0 slice 1 oz','Cheese product, pasteurized process, cheddar, reduced fat',NULL,28,1,0,
'Produkt serowy, proces pasteryzowany, cheddar, obniżona zawartość tłuszczu',60,57,59,58),(16,NULL,'2.0 tbsp',
'Cheese sauce, prepared from recipe',NULL,30,1,0,'Sos serowy, przygotowany z przepisu',64,61,63,62),(17,NULL,
'1.0 piece','Cheese spread, American or Cheddar cheese base, reduced fat',NULL,21,1,0,
'Ser do smarowania, baza amerykańska lub ser cheddar, zredukowany tłuszcz',68,65,67,66),(18,NULL,'1.0 oz',
'Cheese spread, cream cheese base',NULL,28.35,1,0,'Ser do smarowania, baza serów kremowych',72,69,71,70),(19,NULL,
'1.0 cup, diced','Cheese spread, pasteurized process, American',NULL,140,1,0,
'Spread serowy, proces pasteryzowany, amerykański',76,73,75,74),(20,NULL,'1.0 cup, shredded',
'Cheese substitute, mozzarella',NULL,113,1,0,'Zamiennik sera, mozzarella',80,77,79,78),(21,NULL,'1.0 slice',
'Cheese, american cheddar, imitation',NULL,21,1,0,'Ser, amerykański cheddar, imitacja',84,81,83,82),(22,NULL,
'1.0 serving','Cheese, American, nonfat or fat free',NULL,19,1,0,'Ser, amerykański, beztłuszczowy lub beztłuszczowy',88,
85,87,86),(23,NULL,'1.0 oz','Cheese, blue',NULL,28.35,1,0,'Ser, niebieski',92,89,91,90),(24,NULL,'1.0 cup, diced',
'Cheese, brick',NULL,132,1,0,'Ser, cegła',96,93,95,94),(25,NULL,'1.0 cup, mashed','Bananas, raw',NULL,225,9,4,
'Banany, surowe',100,97,99,98);