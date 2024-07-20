package com.example.CatchStudy.repository;

import com.example.CatchStudy.domain.entity.ChatNotification;
import com.example.CatchStudy.domain.entity.ChatRoom;
import com.example.CatchStudy.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatNotificationRepository extends JpaRepository<ChatNotification, Long> {

    Optional<ChatNotification> findFirstByChatRoomAndUserOrderByChatNotificationIdDesc(ChatRoom chatRoom, Users user);

    List<ChatNotification> findAllByChatRoom_ChatRoomIdAndUser_UserId(Long chatRoomId, Long userId);
}
