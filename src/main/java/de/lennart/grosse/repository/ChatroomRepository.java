package de.lennart.grosse.repository;

import de.lennart.grosse.domain.Chatroom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Chatroom entity.
 */
@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    @Query(value = "select distinct chatroom from Chatroom chatroom left join fetch chatroom.members",
        countQuery = "select count(distinct chatroom) from Chatroom chatroom")
    Page<Chatroom> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct chatroom from Chatroom chatroom left join fetch chatroom.members")
    List<Chatroom> findAllWithEagerRelationships();

    @Query("select chatroom from Chatroom chatroom left join fetch chatroom.members where chatroom.id =:id")
    Optional<Chatroom> findOneWithEagerRelationships(@Param("id") Long id);
}
