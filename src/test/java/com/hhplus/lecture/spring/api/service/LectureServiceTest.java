package com.hhplus.lecture.spring.api.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

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

    @DisplayName("존재하지 않은 특강 유니크키의 경우 예외가 발생한다.")
    @Test
    void emptyLectureApply() {
        // given
        long userId = 1;
        long lectureKey = 1;
        LectureApplyRequest request = createLectureApplyRequest(lectureKey, userId);

        given(lectureRepository.findById(request.getLectureKey())).willReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> lectureService.lectureApply(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 특강");
    }

    @DisplayName("존재하지 않은 특강 스케줄을 신청하는 경우 예외가 발생한다.")
    @Test
    void emptyScheduleLectureApply() {
        // given
        long userId = 1;
        long lectureKey = 1;
        LectureApplyRequest request = createLectureApplyRequest(lectureKey, userId);

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        given(lectureRepository.findById(request.getLectureKey())).willReturn(Optional.of(lecture));
        given(lectureScheduleRepository.findById(request.getScheduleKey())).willReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> lectureService.lectureApply(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 특강 스케줄");
    }

    @DisplayName("이미 신청한 특강 스케줄인 경우 예외가 발생한다.")
    @Test
    void alreadyScheduleLectureApply() {
        // given
        long userId = 1;
        long lectureKey = 1;
        LectureApplyRequest request = createLectureApplyRequest(lectureKey, userId);

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        LectureSchedule schedule = createLectureSchedule(lecture);
        given(lectureRepository.findById(request.getLectureKey())).willReturn(Optional.of(lecture));
        given(lectureScheduleRepository.findById(request.getScheduleKey())).willReturn(Optional.ofNullable(schedule));
        given(applicationRepository.findByUserIdAndLectureSchedule(request.getUserId(), schedule)).willReturn(Optional.of(true));

        // when // then
        assertThatThrownBy(() -> lectureService.lectureApply(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 신청한 특강 스케줄");
    }

    @DisplayName("특강 신청 정원이 초과된 경우 예외가 발생한다.")
    @Test
    void mexCountExcess() {
        // given
        long userId = 1;
        long lectureKey = 1;
        LectureApplyRequest request = createLectureApplyRequest(lectureKey, userId);

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        LectureSchedule schedule = createLectureSchedule(lecture);
        given(lectureRepository.findById(request.getLectureKey())).willReturn(Optional.of(lecture));
        given(lectureScheduleRepository.findById(request.getScheduleKey())).willReturn(Optional.ofNullable(schedule));
        given(applicationRepository.countByLectureSchedule(schedule)).willReturn(30L);

        // when // then
        assertThatThrownBy(() -> lectureService.lectureApply(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("특강 스케줄 신청 정원 초과");
    }

    @DisplayName("이미 수강 신청이 되어있는 경우 예외가 발생한다.")
    @Test
    void alreadyLectureApply() {
        // given
        long userId = 1;
        long lectureKey = 1;
        LectureApplyRequest request = createLectureApplyRequest(lectureKey, userId);

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        LectureSchedule schedule = createLectureSchedule(lecture);
        given(lectureRepository.findById(request.getLectureKey())).willReturn(Optional.of(lecture));
        given(lectureScheduleRepository.findById(request.getScheduleKey())).willReturn(Optional.ofNullable(schedule));
        given(applicationRepository.findByUserIdAndLectureSchedule(request.getUserId(), schedule)).willReturn(Optional.of(false));

        // when // then
        assertThatThrownBy(() -> lectureService.lectureApply(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 신청한 특강 스케줄");
    }

//    @DisplayName("특강 신청에 성공 후 특강 신청 정보를 반환한다.")
//    @Test
//    void lectureApply() {
//        // given
//        long userId = 1;
//        long lectureKey = 1;
//        LectureApplyRequest request = createLectureApplyRequest(lectureKey, userId);
//
//        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
//        LectureSchedule schedule = createLectureSchedule(lecture);
//        given(lectureRepository.findById(request.getLectureKey())).willReturn(Optional.of(lecture));
//        given(lectureScheduleRepository.findById(request.getScheduleKey())).willReturn(Optional.ofNullable(schedule));
//        given(applicationRepository.findByUserIdAndLectureSchedule(request.getUserId(), schedule)).willReturn(Optional.empty());
//
//        LectureResponse response = lectureService.lectureApply(request);
//
//        // when // then
//        assertThat(response.getLecture()).isNotNull();
//        assertThat(response.getLecture())
//                .extracting("key", "title")
//                .contains(response.getLecture().getKey(), response.getLecture().getTitle());
//    }

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

    private LectureApplyRequest createLectureApplyRequest(long lectureKey, long userId) {
        return LectureApplyRequest.builder()
                                  .lectureKey(lectureKey)
                                  .userId(userId)
                                  .build();
    }
}