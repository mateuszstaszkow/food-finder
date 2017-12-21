DROP TABLE food_finder.`role_user`;
ALTER TABLE food_finder.`user` ADD role_id bigint(20) NOT NULL;
ALTER TABLE food_finder.`user` ADD CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);