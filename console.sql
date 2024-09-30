drop database if exists news;

create database if not exists news
    character set utf8mb4
    collate utf8mb4_unicode_ci;

use news;

create table if not exists articles
(
    name          varchar(500) primary key not null,
    url           varchar(500)             not null,
    contentUrl    varchar(500),
    description   varchar(500),
    datePublished datetime default now()
);

delete from articles