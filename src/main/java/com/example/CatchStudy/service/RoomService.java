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

        LocalDateTime now = LocalDateTime.now();
        List<LocalTime> dates = new ArrayList<>(); // 선택 할 수 있는 시작 시간 후보

        if(now.toLocalDate().isEqual(selectedDate)){ //오늘이면
            if(endTime.isBefore(startTime)) { //마감 시간이 새벽이면
                if(now.toLocalTime().isBefore(startTime)){ // 현재 새벽이면
                    LocalTime availableTime = LocalTime.of(now.getHour(),0);

                    while (true){
                        if(availableTime.isAfter(now.toLocalTime())){
                            break;
                        }
                        availableTime = availableTime.plusMinutes(30);
                    }
                    while(true){
                        if(!availableTime.isBefore(endTime) || availableTime.plusMinutes(time).isAfter(endTime)){ // 이용 끝나는시간이 마감시간 전이고 시작시간이 마감시간 전인지 확인
                            break;
                        }
                        dates.add(availableTime);
                        availableTime = availableTime.plusMinutes(30);
                    }

                    LocalDateTime availableDate = LocalDateTime.of(selectedDate,startTime); // 8월 5일 9시
                    availableTime = availableDate.toLocalTime();

                    while(true){
                        if(!availableTime.isBefore(LocalTime.MIDNIGHT) && availableTime.isBefore(endTime)) { // 이용 시작시간이 자정을 넘어가는 경우
                            break;
                        }
                        if(availableDate.plusMinutes(time).isAfter(LocalDateTime.of(selectedDate,endTime).plusDays(1))){ // 이용 끝나는 시간이 마감 시간을 넘어간 경우
                            break;
                        }
                        dates.add(availableTime);
                        availableDate = availableDate.plusMinutes(30);
                        availableTime = availableDate.toLocalTime();
                    }
                }else{

                    LocalTime availableTime = LocalTime.of(now.getHour(),0);

                    while(true){
                        if(!availableTime.isBefore(startTime) && availableTime.isAfter(now.toLocalTime())){ //선택한 날짜가 오늘이면 현재 시간 이후 부터 선택 가능
                            break;
                        }
                        availableTime = availableTime.plusMinutes(30);
                    }

                    LocalDateTime availableDate = LocalDateTime.of(selectedDate,availableTime);


                    while(true){
                        if(!availableTime.isBefore(LocalTime.MIDNIGHT) && availableTime.isBefore(endTime)) { // 이용 시작시간이 자정을 넘어가는 경우
                            break;
                        }
                        if(availableDate.plusMinutes(time).isAfter(LocalDateTime.of(selectedDate,endTime).plusDays(1))){ //이용 끝나는 시간이 마감 시간을 넘어간 경우
                            break;

                        }
                        dates.add(availableTime);
                        availableDate = availableDate.plusMinutes(30);
                        availableTime = availableDate.toLocalTime();
                    }
                }
            }else{
                LocalTime availableTime = LocalTime.of(now.getHour(),0);

                while(true){
                    if(!availableTime.isBefore(startTime) && availableTime.isAfter(now.toLocalTime())){ //선택한 날짜가 오늘이면 현재 시간 이후 부터 선택 가능
                        break;
                    }
                    availableTime = availableTime.plusMinutes(30);
                }

                while (true){
                    if(availableTime.plusMinutes(time).isAfter(endTime)){ //퇴실 시간이 영업 마감 시간 이후인 경우
                        break;
                    }
                    dates.add(availableTime);
                    availableTime = availableTime.plusMinutes(30);
                }
            }
        }else{
            if(endTime.isBefore(startTime)){
                LocalTime availableTime = LocalTime.of(0,0);
                while(true){
                    if(!availableTime.isBefore(endTime) || availableTime.plusMinutes(time).isAfter(endTime)){ //이용 끝나는시간이 마감시간 전이고 시작시간이 마감시간 전인지 확인
                        break;
                    }
                    dates.add(availableTime);
                    availableTime = availableTime.plusMinutes(30);
                }

                LocalDateTime availableDate = LocalDateTime.of(selectedDate,startTime); // 8월 5일 9시
                availableTime = availableDate.toLocalTime();

                while(true){
                    if(!availableTime.isBefore(LocalTime.MIDNIGHT) && availableTime.isBefore(endTime)) { // 이용 시작시간이 자정을 넘어가는 경우
                        break;
                    }
                    if(availableDate.plusMinutes(time).isAfter(LocalDateTime.of(selectedDate,endTime).plusDays(1))){ // 이용 끝나는 시간이 마감 시간을 넘어간 경우
                        break;

                    }
                    dates.add(availableTime);
                    availableDate = availableDate.plusMinutes(30);
                    availableTime = availableDate.toLocalTime();
                }
            }else{
                LocalTime availableTime = startTime;
                while (true){
                    if(availableTime.plusMinutes(time).isAfter(endTime)){// 퇴실 시간이 영업 마감 시간 이후인 경우
                        break;
                    }
                    dates.add(availableTime);
                    availableTime = availableTime.plusMinutes(30);
                }
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
