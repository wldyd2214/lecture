package com.hhplus.lecture.spring.domain.schedule;

import com.hhplus.lecture.spring.domain.lecture.Lecture;
import java.util.List;
import java.util.Optional;

public interface LectureScheduleRepository {

    Optional<LectureSchedule> findById(Long scheduleKey);

    Optional<LectureSchedule> findByKeyWithPessimisticLock(Long scheduleKey);

    List<LectureSchedule> findByLecture(Lecture lecture);

    void deleteAllInBatch();

    void save(LectureSchedule schedule);
}
