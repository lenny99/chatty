package de.lennart.grosse.repository;

import de.lennart.grosse.domain.Message;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Message entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select message from Message message where message.user.login = ?#{principal.username}")
    List<Message> findByUserIsCurrentUser();
}
