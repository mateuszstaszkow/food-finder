ALTER TABLE `role_privilege` DROP FOREIGN KEY `FKqq5610tm0bc0iyfl6qgaavpu9`;
ALTER TABLE `role_privilege` DROP INDEX `UK_mitmgq1leeohfmebclq5lgr5g`;
ALTER TABLE `role_privilege` ADD CONSTRAINT `FKqq5610tm0bc0iyfl6qgaavpu9` FOREIGN KEY (`privileges_id`) REFERENCES `privilege` (`id`);

INSERT INTO `role` VALUES (1,'ADMIN');
INSERT INTO `role` VALUES (2,'USER');

INSERT INTO `privilege` VALUES (1,'VIEW_USERS');
INSERT INTO `privilege` VALUES (2,'VIEW_ADMIN');
INSERT INTO `privilege` VALUES (3,'VIEW_PRODUCTS');
INSERT INTO `privilege` VALUES (4,'VIEW_GROUPS');
INSERT INTO `privilege` VALUES (5,'VIEW_DISHES');
INSERT INTO `privilege` VALUES (6,'VIEW_DIETS');
INSERT INTO `privilege` VALUES (7,'VIEW_DAYS');

INSERT INTO `role_privilege` VALUES (1,1);
INSERT INTO `role_privilege` VALUES (1,2);
INSERT INTO `role_privilege` VALUES (1,3);
INSERT INTO `role_privilege` VALUES (1,4);
INSERT INTO `role_privilege` VALUES (1,5);
INSERT INTO `role_privilege` VALUES (1,6);
INSERT INTO `role_privilege` VALUES (1,7);
INSERT INTO `role_privilege` VALUES (2,3);
INSERT INTO `role_privilege` VALUES (2,4);
INSERT INTO `role_privilege` VALUES (2,5);
INSERT INTO `role_privilege` VALUES (2,6);
INSERT INTO `role_privilege` VALUES (2,7);