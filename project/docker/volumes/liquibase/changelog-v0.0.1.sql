--liquibase formatted sql

--changeset asynkov:1 labels:v0.0.1
CREATE TABLE stock
(
    id       text primary key,
    name     text not null,
    category text not null,
    lock     text not null
);

--changeset asynkov:2 labels:v0.0.1
CREATE TABLE stock_snapshot
(
    id        text primary key,
    stock_id  text      not null references stock (id),
    value     decimal   not null,
    timestamp timestamp not null
);