package com.hhplus.lecture.spring.api.service;

import com.hhplus.lecture.spring.api.controller.lecture.dto.common.LectureDTO;
import com.hhplus.lecture.spring.api.controller.lecture.dto.request.LectureApplyRequest;
import com.hhplus.lecture.spring.api.controller.lecture.dto.response.LectureResponse;
import com.hhplus.lecture.spring.domain.application.Application;
import com.hhplus.lecture.spring.domain.application.ApplicationRepository;
import com.hhplus.lecture.spring.domain.lecture.Lecture;
import com.hhplus.lecture.spring.domain.lecture.LectureRepository;
import java.time.LocalDateTime;

import com.hhplus.lecture.spring.domain.schedule.LectureSchedule;
import com.hhplus.lecture.spring.domain.schedule.LectureScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LectureService {
    private final LectureRepository lectureRepository;
    private final LectureScheduleRepository lectureScheduleRepository;
    private final ApplicationRepository applicationRepository;

    public LectureResponse lectureApply(LectureApplyRequest request) {

        Lecture lecture = lectureRepository.findById(request.getLectureKey())
                                           .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 특강"));

        LectureSchedule lectureSchedule = lectureScheduleRepository.findById(request.getScheduleKey())
                                                                   .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 특강 스케줄"));

        if (applicationRepository.findByUserIdAndLectureSchedule(request.getUserId(), request.getScheduleKey())
                                 .isPresent()) {
            throw new IllegalArgumentException("이미 신청한 특강 스케줄");
        }

        long currentApplicationCount = applicationRepository.countByLectureSchedule(lectureSchedule);

        if (lectureSchedule.getMaxCount() <= currentApplicationCount) {
            throw new IllegalArgumentException("해당 특강 스케줄 최대 인원 초과");
        }

        applicationRepository.save(createApplication(request.getUserId(), lectureSchedule));

        LectureDTO lectureDTO =
            createLectureDTO(lecture.getKey(), lecture.getTitle(), lecture.getDesc());

        return LectureResponse.of(lectureDTO);
    }

    private Application createApplication(long userId, LectureSchedule lectureSchedule) {
        return Application.builder()
                          .lectureSchedule(lectureSchedule)
                          .userId(userId)
                          .regDate(LocalDateTime.now())
                          .build();
    }

    private LectureDTO createLectureDTO(long key, String title, String desc) {
        return  LectureDTO.builder()
                          .key(key)
                          .title(title)
                          .desc(desc)
                          .build();
    }
}
