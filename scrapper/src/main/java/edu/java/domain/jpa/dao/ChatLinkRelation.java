package edu.java.domain.jpa.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "link_chat_relation")
public class ChatLinkRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_of_chat", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "id_of_link", nullable = false)
    private Link link;

}
