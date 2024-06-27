package com.hhplus.lecture.spring.api.service;

import com.hhplus.lecture.spring.api.controller.lecture.dto.common.LectureDTO;
import com.hhplus.lecture.spring.api.controller.lecture.dto.request.LectureApplyRequest;
import com.hhplus.lecture.spring.api.controller.lecture.dto.response.LectureListResponse;
import com.hhplus.lecture.spring.api.controller.lecture.dto.response.LectureResponse;
import com.hhplus.lecture.spring.domain.application.Application;
import com.hhplus.lecture.spring.domain.application.ApplicationRepository;
import com.hhplus.lecture.spring.domain.lecture.Lecture;
import com.hhplus.lecture.spring.domain.lecture.LectureRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.hhplus.lecture.spring.domain.schedule.LectureSchedule;
import com.hhplus.lecture.spring.domain.schedule.LectureScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LectureService {
    private final LectureRepository lectureRepository;
    private final LectureScheduleRepository lectureScheduleRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional
    public LectureResponse lectureApply(LectureApplyRequest request) {
        LectureSchedule schedule = applyLectureSchedule(request);

        Lecture lecture = lectureRepository.findById(schedule.getLecture().getKey())
                                           .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 특강"));

        LectureDTO result = LectureDTO.createLectureDTO(lecture.getKey(),
            lecture.getTitle(),
            lecture.getDesc(),
            List.of(schedule));

        return LectureResponse.of(result);
    }

    public LectureSchedule applyLectureSchedule(LectureApplyRequest request) {

        // 비관적락을 통한 동시성 처리
        LectureSchedule schedule =
            lectureScheduleRepository.findByKeyWithPessimisticLock(request.getScheduleKey())
                                     .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 특강 스케줄"));

        if (applicationRepository.findByUserIdAndLectureSchedule(request.getUserId(), schedule).isPresent()) {
            throw new IllegalArgumentException("이미 신청한 특강 스케줄");
        }

        // 현재 인원수 증가 처리
        schedule.currentCountPlus();

        // 신청 완료 사용자의 경우 히스토리 정보 적재
        applicationRepository.save(createApplication(request.getUserId(), schedule));

        return schedule;
    }

    private Application createApplication(long userId, LectureSchedule lectureSchedule) {
        return Application.builder()
                          .lectureSchedule(lectureSchedule)
                          .userId(userId)
                          .regDate(LocalDateTime.now())
                          .build();
    }

    public LectureListResponse getLectures() {
        List<LectureDTO> lectureDTOList = lectureRepository.findAll()
                                                           .stream()
                                                           .map(this::createLectureDTO)
                                                           .collect(Collectors.toList());

        return LectureListResponse.of(lectureDTOList);
    }

    private LectureDTO createLectureDTO(Lecture lecture) {
        List<LectureSchedule> schedules = lectureScheduleRepository.findByLecture(lecture);

        return LectureDTO.createLectureDTO(lecture.getKey(),
            lecture.getTitle(),
            lecture.getDesc(),
            schedules);
    }

    public LectureListResponse getUserApplication(long userId) {

        List<Application> applications = applicationRepository.findByUserId(userId);

        List<LectureSchedule> schedules = applications.stream()
                                                      .map(Application::getLectureSchedule)
                                                      .collect(Collectors.toList());

        List<Lecture> lectures = schedules.stream()
                                          .map(LectureSchedule::getLecture)
                                          .collect(Collectors.toList());

        List<LectureDTO> lectureDTOList = new ArrayList<>();

        for (Lecture lecture : lectures) {
            lectureDTOList.add(
                LectureDTO.createLectureDTO(lecture.getKey(),
                    lecture.getTitle(),
                    lecture.getDesc(),
                    schedules)
            );
        }

        return LectureListResponse.of(lectureDTOList);
    }
}
