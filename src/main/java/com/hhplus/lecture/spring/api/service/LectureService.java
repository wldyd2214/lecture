package com.hhplus.lecture.spring.api.service;

import com.hhplus.lecture.spring.api.ApiResponse;
import com.hhplus.lecture.spring.api.controller.lecture.dto.common.ApplicationDTO;
import com.hhplus.lecture.spring.api.controller.lecture.dto.common.LectureDTO;
import com.hhplus.lecture.spring.api.controller.lecture.dto.common.LectureScheduleDTO;
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

        if (applicationRepository.findByUserIdAndLectureSchedule(request.getUserId(), lectureSchedule)
                                 .isPresent()) {
            throw new IllegalArgumentException("이미 신청한 특강 스케줄");
        }

        long currentApplicationCount = applicationRepository.countByLectureSchedule(lectureSchedule);

        if (lectureSchedule.getMaxCount() <= currentApplicationCount) {
            throw new IllegalArgumentException("특강 스케줄 신청 정원 초과");
        }

        applicationRepository.save(createApplication(request.getUserId(), lectureSchedule));

        LectureDTO lectureDTO =
            createLectureDTO(lecture.getKey(), lecture.getTitle(), lecture.getDesc(), List.of(lectureSchedule));

        return LectureResponse.of(lectureDTO);
    }

    private Application createApplication(long userId, LectureSchedule lectureSchedule) {
        return Application.builder()
                          .lectureSchedule(lectureSchedule)
                          .userId(userId)
                          .regDate(LocalDateTime.now())
                          .build();
    }

    private LectureDTO createLectureDTO(long key, String title, String desc, List<LectureSchedule> schedules) {
        return  LectureDTO.builder()
                          .key(key)
                          .title(title)
                          .desc(desc)
                          .schedules(schedules.stream()
                                              .map(LectureScheduleDTO::toDto)
                                              .collect(Collectors.toList()))
                          .build();
    }

    public LectureListResponse getLectures() {
        List<LectureDTO> lectureDTOList = lectureRepository.findAll().stream()
                                                           .map(this::createLectureDTO)
                                                           .collect(Collectors.toList());

        return LectureListResponse.of(lectureDTOList);
    }

    private LectureDTO createLectureDTO(Lecture lecture) {
        List<LectureSchedule> schedules = lectureScheduleRepository.findByLecture(lecture);

        return createLectureDTO(
                lecture.getKey(),
                lecture.getTitle(),
                lecture.getDesc(),
                schedules
        );
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
                    createLectureDTO(lecture.getKey(),
                                     lecture.getTitle(),
                                     lecture.getDesc(),
                                     schedules)
            );
        }

        return LectureListResponse.of(lectureDTOList);
    }
}
