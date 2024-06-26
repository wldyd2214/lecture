package com.hhplus.lecture.spring.domain.application;

import java.util.List;
import java.util.Optional;

import com.hhplus.lecture.spring.domain.schedule.LectureSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Object> findByUserIdAndLectureSchedule(Long userId, LectureSchedule schedule);

    long countByLectureSchedule(LectureSchedule lectureSchedule);

    List<Application> findByLectureSchedule(LectureSchedule schedule);

    List<Application> findByUserId(long userId);
}
