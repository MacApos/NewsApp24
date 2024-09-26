drop database if exists news;

create database if not exists news
    character set utf8mb4
    collate utf8mb4_unicode_ci;

use news;

create table articles
(
    name          varchar(500) not null primary key,
    url           varchar(500) not null,
    contentUrl    varchar(500) null,
    description   varchar(500) null,
    datePublished datetime default now()
);
