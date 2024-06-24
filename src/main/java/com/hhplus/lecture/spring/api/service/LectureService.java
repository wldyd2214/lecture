package com.hhplus.lecture.spring.api.service;

import com.hhplus.lecture.spring.api.controller.lecture.dto.common.LectureDTO;
import com.hhplus.lecture.spring.api.controller.lecture.dto.request.LectureApplyRequest;
import com.hhplus.lecture.spring.api.controller.lecture.dto.response.LectureResponse;
import com.hhplus.lecture.spring.domain.application.Application;
import com.hhplus.lecture.spring.domain.application.ApplicationRepository;
import com.hhplus.lecture.spring.domain.lecture.Lecture;
import com.hhplus.lecture.spring.domain.lecture.LectureRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LectureService {
    private final LectureRepository lectureRepository;
    private final ApplicationRepository applicationRepository;

    public LectureResponse lectureApply(LectureApplyRequest request) {

        Lecture lecture = lectureRepository.findById(request.getLectureKey())
                                           .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 특강"));

        if (applicationRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new IllegalArgumentException("이미 신청한 특강 존재");
        }

        long currentApplicationCount = applicationRepository.countByLecture(lecture);

        if (lecture.getMaxCount() <= currentApplicationCount) {
            throw new IllegalArgumentException("특강 신청 정원 초과");
        }

        applicationRepository.save(createApplication(request.getUserId(), lecture));

        LectureDTO lectureDTO =
            createLectureDTO(lecture.getKey(), lecture.getTitle(), lecture.getDesc(), lecture.getStartDate());

        return LectureResponse.of(lectureDTO);
    }

    private Application createApplication(long userId, Lecture lecture) {
        return Application.builder()
                          .userId(userId)
                          .lecture(lecture)
                          .regDate(LocalDateTime.now())
                          .build();
    }

    private LectureDTO createLectureDTO(long key, String title, String desc, LocalDateTime startDate) {
        return  LectureDTO.builder()
                          .key(key)
                          .title(title)
                          .desc(desc)
                          .startDate(startDate)
                          .build();
    }
}
