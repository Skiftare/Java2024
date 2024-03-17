--liquibase formatted sql

CREATE TABLE IF NOT EXISTS link_chat_relation (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    id_of_chat BIGINT REFERENCES chat (chat_id),
                                    id_of_link BIGINT REFERENCES link (link_id)
);
