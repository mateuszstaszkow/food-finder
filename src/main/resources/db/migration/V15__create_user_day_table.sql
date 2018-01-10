CREATE TABLE `user_day` (
  `user_id` bigint(20) NOT NULL,
  `day_id` bigint(20) NOT NULL,
  KEY `fk_user_day` (`user_id`),
  CONSTRAINT `fk_user_day_day` FOREIGN KEY (`day_id`) REFERENCES `day` (`id`),
  CONSTRAINT `fk_user_day_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
