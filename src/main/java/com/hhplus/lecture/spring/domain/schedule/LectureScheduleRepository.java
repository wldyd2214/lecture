package com.hhplus.lecture.spring.domain.schedule;

import com.hhplus.lecture.spring.domain.lecture.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureScheduleRepository extends JpaRepository<LectureSchedule, Long> {
    List<LectureSchedule> findByLecture(Lecture lecture);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from LectureSchedule l where l.key = :id")
    Optional<LectureSchedule> findByKeyWithPessimisticLock(@Param("id") Long id);
}
