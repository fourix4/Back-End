package com.example.CatchStudy.domain.dto.response;

import com.example.CatchStudy.domain.entity.Users;
import com.example.CatchStudy.global.enums.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsersResponseDto {

    private Long userId;
    private String userName;
    private String email;
    private Author author;

    public UsersResponseDto(Users entity) {
        this.userId = entity.getUserId();
        this.userName = entity.getUserName();
        this.email = entity.getEmail();
        this.author = entity.getAuthor();
    }
}
