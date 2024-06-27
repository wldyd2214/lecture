package com.hhplus.lecture.spring.domain.application;

import java.util.List;
import java.util.Optional;

import com.hhplus.lecture.spring.domain.schedule.LectureSchedule;

public interface ApplicationRepository {
    Optional<Application> findByUserIdAndLectureSchedule(Long userId, LectureSchedule schedule);

    long countByLectureSchedule(LectureSchedule lectureSchedule);

    List<Application> findByUserId(long userId);

    Application save(Application application);

    void deleteAllInBatch();

    List<Application> findAll();

    List<Application> saveAll(List<Application> applications);
}
