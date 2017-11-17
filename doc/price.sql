create table price(
	id int auto_increment primary key,
	price double not null default 0,
	create_time timestamp not null default current_timestamp
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
create index on price(create_time);