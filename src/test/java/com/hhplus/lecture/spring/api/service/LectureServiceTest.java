package com.hhplus.lecture.spring.api.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

import com.hhplus.lecture.spring.api.controller.lecture.dto.request.LectureApplyRequest;
import com.hhplus.lecture.spring.domain.application.ApplicationRepository;
import com.hhplus.lecture.spring.domain.lecture.Lecture;

import com.hhplus.lecture.spring.domain.lecture.LectureRepository;
import com.hhplus.lecture.spring.domain.schedule.LectureSchedule;
import com.hhplus.lecture.spring.domain.schedule.LectureScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    LectureRepository lectureRepository;

    @Mock
    LectureScheduleRepository lectureScheduleRepository;

    @Mock
    ApplicationRepository applicationRepository;

    @InjectMocks
    LectureService lectureService;

    @DisplayName("존재하지 않은 특강 스케줄을 신청하는 경우 예외가 발생한다.")
    @Test
    void emptyScheduleLectureApply() {
        // given
        long userId = 1;
        long scheduleKey = 1;

        LectureApplyRequest request = createLectureApplyRequest(scheduleKey, userId);

        given(lectureScheduleRepository.findByKeyWithPessimisticLock(request.getScheduleKey()))
            .willThrow(new IllegalArgumentException("존재하지 않는 특강 스케줄"));

        // when // then
        assertThatThrownBy(() -> lectureService.lectureApply(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 특강 스케줄");
    }

    private Lecture createLecture(String title, String desc) {
        return Lecture.builder()
                      .title(title)
                      .desc(desc)
                      .build();
    }

    private LectureSchedule createLectureSchedule(Lecture lecture) {
        return LectureSchedule.builder()
                              .lecture(lecture)
                              .date(LocalDateTime.of(2024, 6, 26, 0, 0))
                              .maxCount(30)
                              .regDate(LocalDateTime.now())
                              .build();
    }

    private LectureApplyRequest createLectureApplyRequest(long scheduleKey, long userId) {
        return LectureApplyRequest.builder()
                                  .scheduleKey(scheduleKey)
                                  .userId(userId)
                                  .build();
    }
}