package com.hhplus.lecture.spring.api.service;

import com.hhplus.lecture.spring.api.controller.lecture.dto.request.LectureApplyRequest;
import com.hhplus.lecture.spring.api.controller.lecture.dto.response.LectureResponse;
import com.hhplus.lecture.spring.domain.application.Application;
import com.hhplus.lecture.spring.domain.application.ApplicationRepository;
import com.hhplus.lecture.spring.domain.lecture.Lecture;
import com.hhplus.lecture.spring.domain.lecture.LectureRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class LectureServiceIntegrationTest {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @AfterEach
    void tearDown() {
        lectureRepository.deleteAllInBatch();
        applicationRepository.deleteAllInBatch();
    }

    @DisplayName("존재하지 않은 특강 유니크키의 경우 예외가 발생한다.")
    @Test
    void emptyLectureApply() {
        // given
        long userId = 1;
        long lectureKey = 9999;

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        lectureRepository.save(lecture);

        LectureApplyRequest request = createLectureApplyRequest(lectureKey, userId);

        // when // then
        assertThatThrownBy(() -> lectureService.lectureApply(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않은 특강");
    }

    @DisplayName("특강 신청 정원이 초과된 경우 예외가 발생한다.")
    @Test
    void mexCountExcess() {
        // given
        long userId = 9999;

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        lectureRepository.save(lecture);

        LectureApplyRequest request = createLectureApplyRequest(lecture.getKey(), userId);

        // TODO: 요런 경우에는 어떤식으로 테스트를 하면 퍼포먼스가 빨라질지 질문! (Q&A)
        // createMexCountApplication(lecture);

        // when // then
        assertThatThrownBy(() -> lectureService.lectureApply(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("특강 신청 정원 초과");
    }

    // 하루에 하나의 수강 신청이 있는 경우 실패
    @DisplayName("이미 수강 신청이 되어있는 경우 예외가 발생한다.")
    @Test
    void alreadyLectureApply() {
        // given
        long userId = 1;

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        lectureRepository.save(lecture);
        applicationRepository.save(createApplication(lecture, userId));

        LectureApplyRequest request = createLectureApplyRequest(lecture.getKey(), userId);

        // when // then
        assertThatThrownBy(() -> lectureService.lectureApply(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 신청한 특강 존재");
    }

    @DisplayName("특강 신청에 성공 후 특강 신청 정보를 반환한다.")
    @Test
    void lectureApply() {
        // given
        long userId = 1;

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        lectureRepository.save(lecture);

        LectureApplyRequest request = createLectureApplyRequest(lecture.getKey(), userId);

        LectureResponse lectureResponse = lectureService.lectureApply(request);

        // when // then
        assertThat(lectureResponse.getLecture()).isNotNull();
        assertThat(lectureResponse.getLecture())
            .extracting("title", "desc")
            .contains(lecture.getTitle(), lecture.getDesc());
    }

    private LectureApplyRequest createLectureApplyRequest(long lectureKey, long userId) {
        return LectureApplyRequest.builder()
                                  .lectureKey(lectureKey)
                                  .userId(userId)
                                  .build();
    }

    private Lecture createLecture(String title, String desc) {
        return Lecture.builder()
                      .title(title)
                      .desc(desc)
                      .regDate(LocalDateTime.now())
                      .build();
    }

//    private void createMexCountApplication(Lecture lecture) {
//        for (int i = 0; i < lecture.getMaxCount(); i++) {
//            Application application = createApplication(lecture, i);
//            applicationRepository.save(application);
//        }
//    }

    private Application createApplication(Lecture lecture, long userId) {
        return Application.builder()
                          .userId(userId)
                          .regDate(LocalDateTime.now())
                          .build();
    }
}