CREATE TABLE IF NOT EXISTS `list_product`(
    `id_list` BIGINT NOT NULL,
    `id_product` BIGINT NOT NULL,
    PRIMARY KEY (`id_list`, `id_product`),
    FOREIGN KEY (`id_list`) REFERENCES `list`(`id`),
    FOREIGN KEY (`id_product`) REFERENCES `product`(`id`)
) ENGINE=InnoDB;