--liquibase formatted sql

--changeset asynkov:1 labels:v0.0.1
CREATE TABLE stock (
	id text primary key,
	name text not null,
	category text not null,
	lock text not null
);