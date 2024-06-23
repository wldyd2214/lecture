package com.hhplus.lecture.spring.api.service;

import com.hhplus.lecture.spring.api.controller.lecture.dto.common.LectureDTO;
import com.hhplus.lecture.spring.api.controller.lecture.dto.request.LectureApplyRequest;
import com.hhplus.lecture.spring.api.controller.lecture.dto.response.LectureResponse;
import com.hhplus.lecture.spring.domain.application.Application;
import com.hhplus.lecture.spring.domain.application.ApplicationRepository;
import com.hhplus.lecture.spring.domain.lecture.Lecture;
import com.hhplus.lecture.spring.domain.lecture.LectureRepository;
import com.hhplus.lecture.spring.domain.user.User;
import com.hhplus.lecture.spring.domain.user.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LectureService {

    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final ApplicationRepository applicationRepository;

    public LectureResponse lectureApply(LectureApplyRequest request) {

        Optional<User> user = userRepository.findById(request.getUserId());

        if (user.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않은 사용자");
        }

        Optional<Lecture> lecture = lectureRepository.findById(request.getLectureKey());

        if (lecture.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않은 특강");
        }

        List<Application> applications = applicationRepository.findAllByLecture(lecture);

        if (lecture.get().getMaxCount() <= applications.size()) {
            throw new IllegalArgumentException("특강 신청 정원 초과");
        }

        return new LectureResponse(LectureDTO.builder()
                                             .key(request.getLectureKey())
                                             .title("허재 코치님의 특강")
                                             .desc("무신사 29cm 전시 시스템 개발자 허재 코치님의 특강")
                                             .startDate(
                                                 LocalDateTime.of(2024, 4, 30, 13, 0, 0))
                                             .build());
    }
}
