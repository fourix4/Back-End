package com.example.CatchStudy.domain.dto;

import com.example.CatchStudy.domain.entity.Users;
import com.example.CatchStudy.global.enums.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsersDto {

    private Long userId;
    private String userName;
    private Author author;

    public static UsersDto from(Users entity) {
        return new UsersDto(
                entity.getUserId(),
                entity.getUserName(),
                entity.getAuthor()
        );
    }
}
