package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.chatRoom.chatRoomId = :chatRoomId ORDER BY m.createDate DESC limit 1")
    Optional<Message> findTop1ByChatRoomIdOrderByCreateDateDesc(@Param("chatRoomId") Long chatRoomId);

    @Query("SELECT m FROM Message m WHERE m.chatRoom.chatRoomId = :chatRoomId ORDER BY m.createDate ASC")
    List<Message> findByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
