package com.example.CatchStudy.domain.entity;

import com.example.CatchStudy.domain.dto.request.MessageRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @Column(columnDefinition = "TEXT")
    private String chat;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "message_image")
    private String MessageImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "char_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    public Message(MessageRequestDto messageRequestDto, Users user, ChatRoom chatRoom) {
        this.chat = messageRequestDto.getChat();
        this.createDate = LocalDateTime.now();
        this.chatRoom = chatRoom;
        this.user = user;
    }
}
