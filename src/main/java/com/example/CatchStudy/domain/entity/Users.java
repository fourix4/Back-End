package com.example.CatchStudy.domain.entity;

import com.example.CatchStudy.global.enums.Author;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Enumerated(EnumType.STRING)
    private Author author;
}
