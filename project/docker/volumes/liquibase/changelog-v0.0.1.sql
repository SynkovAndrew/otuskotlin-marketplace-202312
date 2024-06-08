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

--changeset asynkov:3 labels:v0.0.1
insert into stock (id, name, category, lock) values ('1', 'Gazprom Stock', 'SHARE', '');
insert into stock (id, name, category, lock) values ('2', 'Alfabank Bond', 'BOND', '');
insert into stock (id, name, category, lock) values ('3', 'Tinkoff Stock', 'SHARE', '');

insert into stock_snapshot (id, stock_id, value, timestamp) VALUES ('1', '1', 33.3, '2022-12-12T11:44:00');
insert into stock_snapshot (id, stock_id, value, timestamp) VALUES ('2', '1', 32.3, '2022-12-12T11:45:00');
insert into stock_snapshot (id, stock_id, value, timestamp) VALUES ('3', '1', 33.4, '2022-12-12T11:46:00');
insert into stock_snapshot (id, stock_id, value, timestamp) VALUES ('4', '1', 32.4, '2022-12-12T11:47:00');
insert into stock_snapshot (id, stock_id, value, timestamp) VALUES ('5', '1', 35.4, '2022-12-12T11:48:00');

insert into stock_snapshot (id, stock_id, value, timestamp) VALUES ('6', '2', 23.3, '2022-12-12T11:44:00');
insert into stock_snapshot (id, stock_id, value, timestamp) VALUES ('7', '2', 22.3, '2022-12-12T11:45:00');
insert into stock_snapshot (id, stock_id, value, timestamp) VALUES ('8', '2', 23.4, '2022-12-12T11:46:00');
insert into stock_snapshot (id, stock_id, value, timestamp) VALUES ('9', '2', 22.4, '2022-12-12T11:47:00');
insert into stock_snapshot (id, stock_id, value, timestamp) VALUES ('10', '2', 25.4, '2022-12-12T11:48:00');

insert into stock_snapshot (id, stock_id, value, timestamp) VALUES ('11', '3', 23.3, '2022-12-12T11:44:00');
insert into stock_snapshot (id, stock_id, value, timestamp) VALUES ('12', '3', 22.3, '2022-12-12T11:45:00');
insert into stock_snapshot (id, stock_id, value, timestamp) VALUES ('13', '3', 23.4, '2022-12-12T11:46:00');
insert into stock_snapshot (id, stock_id, value, timestamp) VALUES ('14', '3', 22.4, '2022-12-12T11:47:00');
insert into stock_snapshot (id, stock_id, value, timestamp) VALUES ('15', '3', 25.4, '2022-12-12T11:48:00');