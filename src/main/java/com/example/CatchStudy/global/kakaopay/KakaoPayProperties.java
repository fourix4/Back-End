package com.example.CatchStudy.global.kakaopay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoPayProperties {
    public static String secretKey;
    public static String cid;
    public static String readyUrl;
    public static String approveUrl;

    @Value("${kakaopay.secret-key}")
    public void setAdminKey(String secretKey) {
        KakaoPayProperties.secretKey = secretKey;
    }

    @Value("${kakaopay.cid}")
    public void setCid(String cid) {
        KakaoPayProperties.cid = cid;
    }

    @Value("${kakaopay.ready-url}")
    public void setReadyUrl(String readyUrl) {
        KakaoPayProperties.readyUrl = readyUrl;
    }

    @Value("${kakaopay.approve-url}")
    public void setApproveUrl(String approveUrl) {
        KakaoPayProperties.approveUrl = approveUrl;
    }
}
