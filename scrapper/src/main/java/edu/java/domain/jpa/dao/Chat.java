package edu.java.domain.jpa.dao;

import edu.java.exceptions.entities.LinkNotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long tgChatId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @ManyToMany(cascade = {
        CascadeType.PERSIST
    }, fetch = FetchType.EAGER)

    @JoinTable(name = "link_chat_relation",
               joinColumns = @JoinColumn(name = "id_of_chat", referencedColumnName = "chat_id"),
               inverseJoinColumns = @JoinColumn(name = "id_of_link", referencedColumnName = "id")
    )
    private Set<Link> links = new HashSet<>();

    public void addLink(Link link) {
        this.links.add(link);
        link.getChats().add(this);
    }

    public void removeLink(Link link) {
        boolean removed = this.links.remove(link);
        if (!removed) {
            throw new LinkNotFoundException("Link not found in the chat");
        }
        link.getChats().remove(this);
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Chat chat = (Chat) o;
        return Objects.equals(id, chat.id) && Objects.equals(tgChatId, chat.tgChatId)
            && Objects.equals(createdAt, chat.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tgChatId, createdAt);
    }
}
