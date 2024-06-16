--liquibase formatted sql

--changeset asynkov:1 labels:v0.0.1
CREATE TABLE IF NOT EXISTS test_keyspace.stock
(
    id       text,
    name     text,
    category text,
    lock     text,
    PRIMARY KEY (id)
);

CREATE INDEX ON test_keyspace.stock (name);
