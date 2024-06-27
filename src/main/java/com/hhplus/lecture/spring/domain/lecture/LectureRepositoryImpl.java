package com.hhplus.lecture.spring.domain.lecture;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class LectureRepositoryImpl implements LectureRepository {

    private final LectureJPARepository lectureJPARepository;

    public LectureRepositoryImpl(LectureJPARepository lectureJPARepository) {
        this.lectureJPARepository = lectureJPARepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Lecture> findById(Long lectureKey) {
        return lectureJPARepository.findById(lectureKey);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lecture> findAll() {
        return lectureJPARepository.findAll();
    }

    @Override
    public void deleteAllInBatch() {
        lectureJPARepository.deleteAllInBatch();
    }

    @Override
    public void save(Lecture lecture) {
        lectureJPARepository.save(lecture);
    }
}
