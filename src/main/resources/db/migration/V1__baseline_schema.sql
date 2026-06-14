CREATE TABLE `events` (
    `id` BINARY(16) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `date` TIMESTAMP,
    `capacity` INT NOT NULL,
    `half_price_limit` INT,
    `details` VARCHAR(1000),
    `location` VARCHAR(255),
    `ticket_price` DECIMAL(19, 2),
    PRIMARY KEY (`id`)
);