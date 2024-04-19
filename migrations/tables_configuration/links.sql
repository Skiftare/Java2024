--liquibase formatted sql

--comment: initializing tables
CREATE TABLE IF NOT EXISTS link
(
    id
    INTEGER
    PRIMARY
    KEY
    GENERATED
    ALWAYS AS
    IDENTITY,
    url
    TEXT
    NOT
    NULL,
    created_at
    TIMESTAMP
    WITH
    TIME
    ZONE
    NOT
    NULL
    DEFAULT
    CURRENT_DATE,
    last_update_at
    TIMESTAMP
    WITH
    TIME
    ZONE
    NOT
    NULL
);
