package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.request.RoomsRequestDto;
import com.example.CatchStudy.domain.entity.Room;
import com.example.CatchStudy.domain.entity.StudyCafe;
import com.example.CatchStudy.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;

    public void saveRoom(RoomsRequestDto roomsRequestDto, StudyCafe studyCafe, long cancelAvailableTime) {
        Room room = new Room(roomsRequestDto, studyCafe, cancelAvailableTime);
        roomRepository.save(room);
    }
}
