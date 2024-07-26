package com.example.CatchStudy.service;

import com.example.CatchStudy.domain.dto.response.RoomInfoResponseDto;
import com.example.CatchStudy.domain.dto.response.RoomResponseDto;
import com.example.CatchStudy.domain.entity.Room;
import com.example.CatchStudy.global.exception.CatchStudyException;
import com.example.CatchStudy.global.exception.ErrorCode;
import com.example.CatchStudy.repository.BookedRoomInfoRepository;
import com.example.CatchStudy.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.CatchStudy.domain.dto.request.RoomsRequestDto;
import com.example.CatchStudy.domain.entity.StudyCafe;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;
    private final BookedRoomInfoRepository bookedRoomInfoRepository;

    @Transactional(readOnly = true)
    public List getBookingStatusByDate(Long roomId, String date, Integer time) { //날짜별 예약 현황

        Room room = roomRepository.findByRoomId(roomId).orElseThrow(() -> new CatchStudyException(ErrorCode.ROOM_NOT_FOUND));
        LocalDate selectedDate = LocalDate.of(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)), Integer.parseInt(date.substring(6, 8)));
        LocalTime startTime = room.getStudyCafe().getOpeningHours(); // 영업 시작 시간
        LocalTime endTime = room.getStudyCafe().getClosedHours(); // 영업 마감 시간

        LocalTime nowTime = startTime;
        LocalTime nowTime2 = null; //오늘 선택했을 때
        List<LocalTime> dates = new ArrayList<>(); // 선택 할 수 있는 시작 시간 후보

        LocalDateTime rightNow = LocalDateTime.now();
        LocalDate today = rightNow.toLocalDate();
        LocalTime currentTime = rightNow.toLocalTime();

        if (endTime.isBefore(startTime)) { // 마감시간이 00:00 이후이면

            LocalDateTime now = LocalDateTime.of(selectedDate,startTime);
            LocalDateTime last = LocalDateTime.of(selectedDate,endTime).plusDays(1);
            LocalDateTime now2 = null;

            if(today.isEqual(selectedDate)){ //선택한 날짜가 오늘이면 현재 시간 이후 부터 선택 가능
                now2 = LocalDateTime.of(rightNow.getYear(), rightNow.getMonth(), rightNow.getDayOfMonth(), rightNow.getHour(),0);
                while(true){
                    if(!now2.isBefore(now)&& now2.isAfter(rightNow)){
                        break;
                    }
                    now2 = now2.plusMinutes(30);
                }
                now = now2;
            }

            while(true){
                if(now.plusMinutes(time).isAfter(last)){
                    break;
                }
                dates.add(LocalTime.of(now.getHour(),now.getMinute()));
                now = now.plusMinutes(30);
            }

        }else{

            if(today.isEqual(selectedDate)){ //선택한 날짜가 오늘이면 현재 시간 이후 부터 선택 가능

                nowTime2 = LocalTime.of(rightNow.getHour(),0);
                while(true){
                    if(!nowTime2.isBefore(startTime)&& nowTime2.isAfter(currentTime)){
                        break;
                    }
                    nowTime2 = nowTime2.plusMinutes(30);
                }
                nowTime = nowTime2;
            }

            while(true){
                if(nowTime.plusMinutes(time).isAfter(endTime)){ // 퇴실 시간이 영업 마감 시간 이후인 경우
                    break;
                }
                dates.add(nowTime);
                nowTime=nowTime.plusMinutes(30);
            }

        }

        //선택한 시간 안에 예약된 스터디룸이 없을 때만 가능
        return dates.stream().filter(
                s -> bookedRoomInfoRepository.existsBookedRoom(
                                roomId,
                                LocalDateTime.of(
                                        selectedDate.getYear(),
                                        selectedDate.getMonth(),
                                        selectedDate.getDayOfMonth(),
                                        s.getHour(),
                                        s.getMinute()
                                ),
                                LocalDateTime.of(
                                        selectedDate.getYear(),
                                        selectedDate.getMonth(),
                                        selectedDate.getDayOfMonth(),
                                        s.getHour(),
                                        s.getMinute()).plusMinutes(time)
                        )
                        .equals(0)
        ).map(String::valueOf).collect(Collectors.toList());
    }

    @Transactional
    public void saveRoom(RoomsRequestDto roomsRequestDto, StudyCafe studyCafe, long cancelAvailableTime) {
        Room room = new Room(roomsRequestDto, studyCafe, cancelAvailableTime);
        roomRepository.save(room);
    }

    @Transactional
    public void deleteRoom(long cafeId) {
        roomRepository.deleteAllByStudyCafe_CafeId(cafeId);
    }

    public RoomInfoResponseDto getRoomInfoResponseDto(long cafeId) {
        List<RoomResponseDto> roomResponseDtoList = roomRepository.findAllByStudyCafeCafeId(cafeId).stream().map(RoomResponseDto::new).toList();
        Long cancelAvailableTime = roomRepository.findCancelAvailableTimeByCafeId(cafeId).orElse(0L);

        return new RoomInfoResponseDto(cancelAvailableTime, roomResponseDtoList);
    }
}
