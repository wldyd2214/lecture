package com.hhplus.lecture.spring.domain.application;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

import com.hhplus.lecture.spring.domain.schedule.LectureSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Object> findByUserIdAndLectureSchedule(Long userId, LectureSchedule schedule);

    long countByLectureSchedule(LectureSchedule lectureSchedule);

    List<Application> findByUserId(long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    //@Lock(LockModeType.PESSIMISTIC_READ)
    //@Query("select l from Application l where l.key = :id")
    Application findWithPessimisticLockById(Long id);
}
