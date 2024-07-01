package com.example.CatchStudy.domain.entity;

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
}
