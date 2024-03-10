--liquibase formatted sql

CREATE TABLE IF NOT EXISTS database.link_chat_relation (
                                    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                    id_of_chat BIGINT REFERENCES database.chat(chat_id),
                                    id_of_link BIGINT REFERENCES database.link(link_id)
);
