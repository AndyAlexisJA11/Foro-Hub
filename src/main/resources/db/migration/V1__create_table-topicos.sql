CREATE TABLE topicos (
id bigint not null auto_increment,
titulo varchar(255) not null unique,
mensaje text not null,
fechaCreacion datetime not null default current_timestamp,
status varchar(50) not null,
autor varchar(250) not null,
curso varchar(50) not null,

primary key (id)
);

