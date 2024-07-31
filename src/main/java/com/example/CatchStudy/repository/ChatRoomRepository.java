package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM ChatRoom c WHERE c.user.userId = :userId")
    List<ChatRoom> findByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM ChatRoom c WHERE c.studyCafe.cafeId = :cafeId")
    List<ChatRoom> findByCafeId(@Param("cafeId") Long cafeId);

    @Query("SELECT c FROM ChatRoom c WHERE c.user.userId = :userId AND c.studyCafe.cafeId = :cafeId")
    Optional<ChatRoom> findByUserIdAndCafeId(@Param("userId") Long userId, @Param("cafeId") Long cafeId);

}
