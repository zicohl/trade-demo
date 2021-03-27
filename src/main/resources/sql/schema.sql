create table transaction
(
id BIGINT(20) not null AUTO_INCREMENT ,
trade_id BIGINT(20) not null,
version INT not null,
security_code varchar(20) not null,
quantity INT not null,
operation_type TINYINT not null,
direction_type TINYINT not null,
created_time timestamp NOT NULL,
primary key(id)
);

create table trade_status
(
trade_id BIGINT(20) not null,
version INT not null,
security_code varchar(20) not null,
quantity INT not null,
operation_type TINYINT not null,
direction_type TINYINT not null,
primary key(trade_id)
);

create table position
(
security_code varchar(20) not null,
quantity INT not null,
primary key(security_code)
);

commit;
