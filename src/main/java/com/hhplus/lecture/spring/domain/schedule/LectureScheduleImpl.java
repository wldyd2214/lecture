package com.hhplus.lecture.spring.domain.schedule;

import com.hhplus.lecture.spring.domain.lecture.Lecture;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class LectureScheduleImpl implements LectureScheduleRepository {

    private final LectureScheduleJPARepository lectureScheduleJPARepository;

    public LectureScheduleImpl(LectureScheduleJPARepository lectureScheduleJPARepository) {
        this.lectureScheduleJPARepository = lectureScheduleJPARepository;
    }

    @Override
    public Optional<LectureSchedule> findById(Long scheduleKey) {
        return lectureScheduleJPARepository.findById(scheduleKey);
    }

    @Override
    public Optional<LectureSchedule> findByIdWithPessimisticLock(Long scheduleKey) {
        return lectureScheduleJPARepository.findByIdWithPessimisticLock(scheduleKey);
    }

    @Override
    public List<LectureSchedule> findByLecture(Lecture lecture) {
        return lectureScheduleJPARepository.findByLecture(lecture);
    }

    @Override
    public void deleteAllInBatch() {
        lectureScheduleJPARepository.deleteAllInBatch();
    }

    @Override
    public void save(LectureSchedule schedule) {
        lectureScheduleJPARepository.save(schedule);
    }
}
