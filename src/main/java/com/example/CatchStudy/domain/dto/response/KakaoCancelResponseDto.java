package com.example.CatchStudy.domain.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class KakaoCancelResponseDto {

    private String aid; // 요청 고유 번호
    private String tid; // 결제 고유 번호
    private String cid; // 가맹점 코드
    private String status; // 결제 상태
    private KakaoAmountResponseDto amount; // 결제 금액 정보, 결제 요청 구현할때 이미 구현해놓음
    private KakaoApprovedCancelAmountDto approved_cancel_amount; // 이번 요청으로 취소된 금액
    private String item_name; // 상품 이름
    private String item_code; // 상품 코드
    private int quantity; // 상품 수량
    private LocalDateTime created_at; // 결제 준비 요청 시각
    private LocalDateTime approved_at; // 결제 승인 시각
    private LocalDateTime canceled_at; // 결제 취소 시각
    private String payload; // 취소 요청 시 전달한 값
}
