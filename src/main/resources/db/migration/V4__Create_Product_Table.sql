CREATE TABLE IF NOT EXISTS `product`(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(180) NOT NULL,
    `description` VARCHAR(240),
    `picture` TEXT,
    `bought` BIT(1) DEFAULT NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB;