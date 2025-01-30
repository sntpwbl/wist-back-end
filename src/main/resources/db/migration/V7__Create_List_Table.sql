CREATE TABLE IF NOT EXISTS `list`(
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(180) NOT NULL,
    `description` TEXT,
    `created_at` DATE NOT NULL,
    `user_id` BIGINT NOT NULL,
    FOREIGN KEY(`user_id`) REFERENCES `users`(`id`)
) ENGINE=InnoDB;