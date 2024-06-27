package com.hhplus.lecture.spring.domain.application;

import com.hhplus.lecture.spring.domain.schedule.LectureSchedule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationJPARepository extends JpaRepository<Application, Long> {

    Optional<Application> findByUserIdAndLectureSchedule(Long userId, LectureSchedule schedule);

    long countByLectureSchedule(LectureSchedule schedule);

    List<Application> findByUserId(long userId);
}
