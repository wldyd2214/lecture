package com.hhplus.lecture.spring.domain.schedule;

import com.hhplus.lecture.spring.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureScheduleRepository extends JpaRepository<LectureSchedule, Long> {
    List<LectureSchedule> findByLecture(Lecture lecture);
}
