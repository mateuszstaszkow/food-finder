DROP TABLE `role_user`;
ALTER TABLE `user` ADD role_id bigint(20) NOT NULL;
ALTER TABLE `user` ADD CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);