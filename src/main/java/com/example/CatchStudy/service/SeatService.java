package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.entity.Seat;
import com.example.CatchStudy.domain.entity.StudyCafe;
import com.example.CatchStudy.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class SeatService {

    private final SeatRepository seatRepository;

    public void saveSeat(String seatNumber, StudyCafe studyCafe) {
        Seat seat = new Seat(seatNumber, studyCafe);
        seatRepository.save(seat);
    }

    public void deleteSeat(long cafeId) {
        seatRepository.deleteAllByStudyCafe_CafeId(cafeId);
    }
}
