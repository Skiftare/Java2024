--liquibase formatted sql

--comment: initializing tables
CREATE TABLE IF NOT EXISTS link (
                                    link_id BIGINT PRIMARY KEY NOT NULL,
                                    url TEXT NOT NULL,
                                    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_DATE,
                                    last_update_at TIMESTAMP WITH TIME ZONE NOT NULL
);
