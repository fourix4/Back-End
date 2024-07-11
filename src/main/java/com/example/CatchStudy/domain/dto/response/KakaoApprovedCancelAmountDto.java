package com.example.CatchStudy.domain.dto.response;

import lombok.Getter;

@Getter
public class KakaoApprovedCancelAmountDto {

    private int total; // 이번 요청으로 취소된 전체 금액
    private int tax_free; // 이번 요청으로 취소된 비과세 금액
    private int vat; // 이번 요청으로 취소된 부가세 금액
}
