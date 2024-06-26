--liquibase formatted sql
--comment: initializing tables
CREATE TABLE IF NOT EXISTS chat (
    id      BIGINT GENERATED ALWAYS AS IDENTITY,
    chat_id BIGINT PRIMARY KEY NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_DATE
);
