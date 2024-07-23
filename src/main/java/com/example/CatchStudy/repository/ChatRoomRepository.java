package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM ChatRoom c WHERE c.user.userId = :userId")
    List<ChatRoom> findByUserId(@Param("userId") Long userId);

}
