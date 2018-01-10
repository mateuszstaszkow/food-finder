INSERT INTO `user` VALUES (2, "user@foodfinder.com", "", "", 0, 0, 0, "", 1, "$2a$10$uhBXm6h8VpX0hq.VEdtqle7xLKo2pksdBcaUxgh0Rau2XGQHsO2sm", 1);

INSERT INTO `privilege` VALUES (8,'RECOGNIZE');
INSERT INTO `privilege` VALUES (9,'MIGRATE');
INSERT INTO `privilege` VALUES (10,'TRANSLATE');

INSERT INTO `role_privilege` VALUES (1,8);
INSERT INTO `role_privilege` VALUES (1,9);
INSERT INTO `role_privilege` VALUES (1,10);