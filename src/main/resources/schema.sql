CREATE TABLE IF NOT EXISTS `customers` (
    `customer_id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL,
    `email` VARCHAR(50) NOT NULL,
    `mobile_number` VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS `accounts` (
    `account_id` INT AUTO_INCREMENT PRIMARY KEY,
    `account_number` INT NOT NULL,
    `customer_id` INT NOT NULL,
    `account_type` VARCHAR(50) NOT NULL,
    `branch_address` VARCHAR(100) NOT NULL
);

create table users(
    username varchar_ignorecase(50) not null primary key,
    password varchar_ignorecase(500) not null,
    enabled boolean not null
    );
create table authorities (
    username varchar_ignorecase(50) not null,
    authority varchar_ignorecase(50) not null,
    constraint fk_authorities_users foreign key(username) references users(username)
    );

create unique index ix_auth_username on authorities (username,authority);