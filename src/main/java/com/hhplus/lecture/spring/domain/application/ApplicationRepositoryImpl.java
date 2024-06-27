package com.hhplus.lecture.spring.domain.application;

import com.hhplus.lecture.spring.domain.schedule.LectureSchedule;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationRepositoryImpl implements ApplicationRepository {

    private final ApplicationJPARepository applicationJPARepository;

    public ApplicationRepositoryImpl(ApplicationJPARepository applicationJPARepository) {
        this.applicationJPARepository = applicationJPARepository;
    }

    @Override
    public Optional<Application> findByUserIdAndLectureSchedule(Long userId, LectureSchedule schedule) {
        return applicationJPARepository.findByUserIdAndLectureSchedule(userId, schedule);
    }

    @Override
    public long countByLectureSchedule(LectureSchedule schedule) {
        return applicationJPARepository.countByLectureSchedule(schedule);
    }

    @Override
    public List<Application> findByUserId(long userId) {
        return applicationJPARepository.findByUserId(userId);
    }

    @Override
    public Application save(Application application) {
        return applicationJPARepository.save(application);
    }

    @Override
    public void deleteAllInBatch() {
        applicationJPARepository.deleteAllInBatch();
    }

    @Override
    public List<Application> findAll() {
        return applicationJPARepository.findAll();
    }

    @Override
    public List<Application> saveAll(List<Application> applications) {
        return applicationJPARepository.saveAll(applications);
    }
}
