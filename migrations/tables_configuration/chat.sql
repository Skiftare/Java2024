--liquibase formatted sql
ALTER SYSTEM SET lc_messages TO 'English_United States.1252';
ALTER SYSTEM SET lc_monetary TO 'English_United States.1252';
ALTER SYSTEM SET lc_numeric TO 'English_United States.1252';
ALTER SYSTEM SET lc_time TO 'English_United States.1252';
ALTER SYSTEM SET default_text_search_config TO 'pg_catalog.english';
--comment: initializing tables
CREATE TABLE IF NOT EXISTS chat (
    id      BIGINT GENERATED ALWAYS AS IDENTITY,
    chat_id BIGINT PRIMARY KEY NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_DATE
);
