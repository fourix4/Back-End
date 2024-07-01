package com.example.CatchStudy.domain.entity;

import com.example.CatchStudy.global.enums.Author;
import com.example.CatchStudy.global.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name="users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column
    @Enumerated
    private Enum<Gender> gender;

    @Column(name = "user_phone")
    private String userPhone;

    @Column
    private LocalDateTime birth;

    @Column
    @Enumerated
    private Enum<Author> author;
}
