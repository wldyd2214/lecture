package com.hhplus.lecture.spring.domain.application;

import com.hhplus.lecture.spring.domain.lecture.Lecture;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<Application> findByUserId(Long userId);

    long countByLecture(Lecture lecture);
}
