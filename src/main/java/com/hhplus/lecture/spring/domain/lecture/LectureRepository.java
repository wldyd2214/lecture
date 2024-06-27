package com.hhplus.lecture.spring.domain.lecture;

import java.util.List;
import java.util.Optional;

public interface LectureRepository {

    Optional<Lecture> findById(Long lectureKey);

    List<Lecture> findAll();

    void deleteAllInBatch();

    void save(Lecture lecture);
}
