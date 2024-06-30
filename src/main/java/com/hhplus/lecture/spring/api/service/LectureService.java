package com.hhplus.lecture.spring.api.service;

import com.hhplus.lecture.spring.domain.application.Application;
import com.hhplus.lecture.spring.domain.application.ApplicationRepository;
import com.hhplus.lecture.spring.domain.lecture.Lecture;
import com.hhplus.lecture.spring.domain.lecture.LectureRepository;
import java.time.LocalDateTime;
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
    public LectureSchedule lectureApply(long scheduleKey, long userId) {

        LectureSchedule schedule =
            lectureScheduleRepository.findByIdWithPessimisticLock(scheduleKey)
                                     .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 특강 스케줄"));

        if (applicationRepository.findByUserIdAndLectureSchedule(userId, schedule).isPresent()) {
            throw new IllegalArgumentException("이미 신청한 특강 스케줄");
        }

        schedule.currentCountPlus();

        ApplicationHistorySave(schedule, userId);

        return schedule;
    }

    private Application ApplicationHistorySave(LectureSchedule schedule, long userId) {
        Application application =
            Application.builder().lectureSchedule(schedule)
                       .userId(userId)
                       .regDate(LocalDateTime.now())
                       .build();

        return applicationRepository.save(application);
    }

    public List<Lecture> getLectures() {
        return lectureRepository.findAll();
    }

    public List<Lecture> getUserApplication(long userId) {

        List<Application> applications =
            applicationRepository.findByUserId(userId);

        List<LectureSchedule> schedules =
            applications.stream()
                        .map(Application::getLectureSchedule)
                        .collect(Collectors.toList());

        List<Lecture> lectures =
            schedules.stream()
                     .map(LectureSchedule::getLecture)
                     .collect(Collectors.toList());

        return lectures;
    }
}
