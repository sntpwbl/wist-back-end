CREATE TABLE IF NOT EXISTS `store_link` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `store` VARCHAR(180) NOT NULL,
    `url` TEXT NOT NULL,
    `product_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB;