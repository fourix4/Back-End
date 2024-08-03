package com.example.CatchStudy.global.oauth;

import com.example.CatchStudy.domain.entity.Users;
import com.example.CatchStudy.global.enums.Author;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class GoogleProfile {
    private String id;
    private String email;
    private String verified_email;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String locale;

    public Users toEntity() {
        return Users.builder()
                .userName(name)
                .email(email)
                .author(Author.roleUser)
                .build();
    }
}