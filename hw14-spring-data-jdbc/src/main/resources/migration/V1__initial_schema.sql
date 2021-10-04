create table client
(
    id       bigserial not null primary key,
    name     varchar(50),
    password varchar(50),
    login    varchar(50)
);
