package com.hhplus.lecture.spring.domain.lecture;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureJPARepository extends JpaRepository<Lecture, Long> {
}
