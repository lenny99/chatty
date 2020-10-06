package de.lennart.grosse.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Chatroom.
 */
@Entity
@Table(name = "chatroom")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Chatroom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "chatroom_name")
    private String chatroomName;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "chatroom_members",
               joinColumns = @JoinColumn(name = "chatroom_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "members_id", referencedColumnName = "id"))
    private Set<User> members = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public Chatroom chatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
        return this;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public Set<User> getMembers() {
        return members;
    }

    public Chatroom members(Set<User> users) {
        this.members = users;
        return this;
    }

    public Chatroom addMembers(User user) {
        this.members.add(user);
        return this;
    }

    public Chatroom removeMembers(User user) {
        this.members.remove(user);
        return this;
    }

    public void setMembers(Set<User> users) {
        this.members = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Chatroom)) {
            return false;
        }
        return id != null && id.equals(((Chatroom) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Chatroom{" +
            "id=" + getId() +
            ", chatroomName='" + getChatroomName() + "'" +
            "}";
    }
}
