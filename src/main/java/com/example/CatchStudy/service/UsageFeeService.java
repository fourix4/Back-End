package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.request.UsageFeeRequestDto;
import com.example.CatchStudy.domain.dto.response.UsageFeeResponseDto;
import com.example.CatchStudy.domain.entity.StudyCafe;
import com.example.CatchStudy.domain.entity.UsageFee;
import com.example.CatchStudy.repository.UsageFeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UsageFeeService {

    private final UsageFeeRepository usageFeeRepository;

    @Transactional
    public void saveUsageFee(UsageFeeRequestDto usageFeeRequestDto, StudyCafe studyCafe) {
        UsageFee usageFee = new UsageFee(usageFeeRequestDto, studyCafe);
        usageFeeRepository.save(usageFee);
    }

    @Transactional
    public void deleteUsageFee(long cafeId) {
        usageFeeRepository.deleteAllByStudyCafe_CafeId(cafeId);
    }

    public List<UsageFeeResponseDto> getUsageFeeResponseDto(long cafeId) {

        return usageFeeRepository.findAllByStudyCafe_CafeId(cafeId).stream().
                map(UsageFeeResponseDto::new).toList();
    }
}
