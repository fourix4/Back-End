package com.example.CatchStudy.global.oauth;

import com.example.CatchStudy.domain.entity.Users;
import com.example.CatchStudy.global.enums.Author;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
public class OAuth2Attribute {

    private Map<String, Object> attributes; // 사용자 속성 정보 담는 Map
    private String attributeKey;            // 사용자 속성의 키 값
    private String email;                   // email로 중복 검사
    private String name;
    private String provider;                // 제공자 정보

    // 서비스에 따라 OAuth2Attribute 객체를 생성하는 메서드
    static OAuth2Attribute of(String provider, String attributeKey,
                              Map<String, Object> attributes) {
        return switch (provider) {
            case "kakao" -> ofKakao("email", attributes);       // sub : 사용자 회원번호
            //case "google" -> ofGoogle(provider, attributeKey, attributes);
            //case "naver" -> ofNaver(provider, "id", attributes);
            default -> throw new RuntimeException();
        };
    }

    /*
     *   Kakao 로그인일 경우 사용하는 메서드, 필요한 사용자 정보가 kakaoAccount -> kakaoProfile 두번 감싸져 있어서,
     *   두번 get() 메서드를 이용해 사용자 정보를 담고있는 Map을 꺼내야한다.
     * */
    private static OAuth2Attribute ofKakao(String attributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .provider("kakao")
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .build();
    }

    public Users toEntity() {
        return Users.builder()
                .userName(name)
                .email(email)
                .author(Author.roleUser)
                .build();
    }
}
