CREATE DATABASE trade;
CREATE USER 'hsbc'@'%' IDENTIFIED BY 'hsbc123456';
GRANT ALL PRIVILEGES ON *.* TO 'hsbc'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

CREATE TABLE t_account
(
    id          BIGINT(20) NOT NULL AUTO_INCREMENT,
    account_id  VARCHAR(255)   NOT NULL,
    amount      DECIMAL(19, 4) NOT NULL,
    username  varchar(255)   NOT NULL,
    user_id   varchar(255),
    create_time TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_delete   INT            NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE t_transaction
(
    id             BIGINT(20) NOT NULL AUTO_INCREMENT,
    transaction_id VARCHAR(255)   NOT NULL,
    source_account VARCHAR(255)   NOT NULL,
    dest_account   VARCHAR(255)   NOT NULL,
    timestamp      BIGINT(20) NOT NULL,
    amount         DECIMAL(19, 4) NOT NULL,
    create_time    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_delete      INT            NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `t_user`
(
    `username`    varchar(255) NOT NULL,
    `password`    varchar(255) NOT NULL,
    `user_id`     varchar(255),
    `phone_no`    varchar(20),
    `gender`      tinyint,
    `birthday`    date,
    `description` text,
    `create_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `is_delete`   int          NOT NULL DEFAULT 0,
    PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


insert into t_account(account_id,username,user_id,amount,create_time,update_time,is_delete) values("c684fa083ca14fe8ad8332b1f1d7c4aa","hsbc",1,100,now(),now(),0);
insert into t_account(account_id,username,user_id,amount,create_time,update_time,is_delete) values("3da6111a917b4aa09a0cce02e133c85a","colin",2,200,now(),now(),0);
