package com.hhplus.lecture.spring.domain.schedule;

import com.hhplus.lecture.spring.domain.lecture.Lecture;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface LectureScheduleJPARepository extends JpaRepository<LectureSchedule, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ls from LectureSchedule ls where ls.key = :key")
    Optional<LectureSchedule> findByIdWithPessimisticLock(long key);

    List<LectureSchedule> findByLecture(Lecture lecture);
}
