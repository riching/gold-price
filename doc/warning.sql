create table warning_condition(
	id int auto_increment primary key,
	start_hour int not null default 0,
	end_hour int not null default 0,
	status int not null default 0,
	update_time timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=utf8;