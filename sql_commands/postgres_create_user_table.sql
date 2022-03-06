create table "User"
(
	id serial,
	name VARCHAR not null,
	username VARCHAR not null,
	status VARCHAR,
	token VARCHAR
);

create unique index user_id_uindex
	on "User" (id);

create unique index user_username_uindex
	on "User" (username);

alter table "User"
	add constraint user_pk
		primary key (id);