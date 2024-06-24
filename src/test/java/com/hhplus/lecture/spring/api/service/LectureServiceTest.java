package com.hhplus.lecture.spring.api.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.hhplus.lecture.spring.api.controller.lecture.dto.request.LectureApplyRequest;
import com.hhplus.lecture.spring.domain.application.Application;
import com.hhplus.lecture.spring.domain.application.ApplicationRepository;
import com.hhplus.lecture.spring.domain.lecture.Lecture;
import com.hhplus.lecture.spring.domain.lecture.LectureRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class LectureServiceTest {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @AfterEach
    void tearDown() {
        lectureRepository.deleteAllInBatch();
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
        createMexCountApplication(lecture);

        // when // then
        assertThatThrownBy(() -> lectureService.lectureApply(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("특강 신청 정원 초과");
    }

    private void createMexCountApplication(Lecture lecture) {
        for (int i = 0; i < lecture.getMaxCount(); i++) {
            Application application = createApplication(lecture, i);
            applicationRepository.save(application);
        }
    }
    // 하루에 하나의 수강 신청이 있는 경우 실패
    // 특강 신청시간이 아닌 경우 실패


//    @DisplayName("특강 신청을 성공한다.")
//    @Test
//    void lectureApply() {
//        given()
//    }

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
                      .startDate(LocalDateTime.of(2024, 4, 30, 13, 0, 0))
                      .maxCount(30)
                      .build();
    }

    private Application createApplication(Lecture lecture, long userId) {
        return Application.builder()
                          .lecture(lecture)
                          .userId(userId)
                          .regDate(LocalDateTime.now())
                          .build();
    }
}