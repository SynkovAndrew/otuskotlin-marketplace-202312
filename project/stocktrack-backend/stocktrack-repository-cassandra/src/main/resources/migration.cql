CREATE KEYSPACE IF NOT EXISTS test_keyspace
    WITH REPLICATION = {
        'class' : 'SimpleStrategy',
        'replication_factor' : 1
        };

CREATE TABLE IF NOT EXISTS test_keyspace.stock
(
    id       text,
    name     text,
    category text,
    lock     text,
    PRIMARY KEY (id)
);