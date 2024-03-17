/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq.generated.tables.records;

import edu.java.domain.jooq.generated.tables.LinkChatRelation;
import java.beans.ConstructorProperties;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class LinkChatRelationRecord extends UpdatableRecordImpl<LinkChatRelationRecord>
    implements Record3<Long, Long, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Create a detached LinkChatRelationRecord
     */
    public LinkChatRelationRecord() {
        super(LinkChatRelation.LINK_CHAT_RELATION);
    }

    /**
     * Create a detached, initialised LinkChatRelationRecord
     */
    @ConstructorProperties({"id", "idOfChat", "idOfLink"})
    public LinkChatRelationRecord(@Nullable Long id, @Nullable Long idOfChat, @Nullable Long idOfLink) {
        super(LinkChatRelation.LINK_CHAT_RELATION);

        setId(id);
        setIdOfChat(idOfChat);
        setIdOfLink(idOfLink);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised LinkChatRelationRecord
     */
    public LinkChatRelationRecord(edu.java.domain.jooq.generated.tables.pojos.LinkChatRelation value) {
        super(LinkChatRelation.LINK_CHAT_RELATION);

        if (value != null) {
            setId(value.getId());
            setIdOfChat(value.getIdOfChat());
            setIdOfLink(value.getIdOfLink());
            resetChangedOnNotNull();
        }
    }

    /**
     * Getter for <code>LINK_CHAT_RELATION.ID</code>.
     */
    @Nullable
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>LINK_CHAT_RELATION.ID</code>.
     */
    public void setId(@Nullable Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>LINK_CHAT_RELATION.ID_OF_CHAT</code>.
     */
    @Nullable
    public Long getIdOfChat() {
        return (Long) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>LINK_CHAT_RELATION.ID_OF_CHAT</code>.
     */
    public void setIdOfChat(@Nullable Long value) {
        set(1, value);
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>LINK_CHAT_RELATION.ID_OF_LINK</code>.
     */
    @Nullable
    public Long getIdOfLink() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>LINK_CHAT_RELATION.ID_OF_LINK</code>.
     */
    public void setIdOfLink(@Nullable Long value) {
        set(2, value);
    }

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    @Override
    @NotNull
    public Row3<Long, Long, Long> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row3<Long, Long, Long> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return LinkChatRelation.LINK_CHAT_RELATION.ID;
    }

    @Override
    @NotNull
    public Field<Long> field2() {
        return LinkChatRelation.LINK_CHAT_RELATION.ID_OF_CHAT;
    }

    @Override
    @NotNull
    public Field<Long> field3() {
        return LinkChatRelation.LINK_CHAT_RELATION.ID_OF_LINK;
    }

    @Override
    @Nullable
    public Long component1() {
        return getId();
    }

    @Override
    @Nullable
    public Long component2() {
        return getIdOfChat();
    }

    @Override
    @Nullable
    public Long component3() {
        return getIdOfLink();
    }

    @Override
    @Nullable
    public Long value1() {
        return getId();
    }

    @Override
    @Nullable
    public Long value2() {
        return getIdOfChat();
    }

    @Override
    @Nullable
    public Long value3() {
        return getIdOfLink();
    }

    @Override
    @NotNull
    public LinkChatRelationRecord value1(@Nullable Long value) {
        setId(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public LinkChatRelationRecord value2(@Nullable Long value) {
        setIdOfChat(value);
        return this;
    }

    @Override
    @NotNull
    public LinkChatRelationRecord value3(@Nullable Long value) {
        setIdOfLink(value);
        return this;
    }

    @Override
    @NotNull
    public LinkChatRelationRecord values(@Nullable Long value1, @Nullable Long value2, @Nullable Long value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }
}
