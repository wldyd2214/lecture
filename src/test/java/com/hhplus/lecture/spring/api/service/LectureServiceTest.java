package com.hhplus.lecture.spring.api.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.hhplus.lecture.spring.api.controller.lecture.dto.request.LectureApplyRequest;
import com.hhplus.lecture.spring.domain.application.Application;
import com.hhplus.lecture.spring.domain.application.ApplicationRepository;
import com.hhplus.lecture.spring.domain.lecture.Lecture;
import com.hhplus.lecture.spring.domain.lecture.LectureRepository;
import com.hhplus.lecture.spring.domain.user.User;
import com.hhplus.lecture.spring.domain.user.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
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
    private UserRepository userRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        lectureRepository.deleteAllInBatch();
    }

    // 존재하지 않은 사용자 유니크키로 특강을 신청하는 경우
    @DisplayName("존재하지 않은 사용자 유니크키의 경우 예외가 발생한다.")
    @Test
    void emptyUserLectureApply() {
        // given
        long lectureKey = 1;
        long userId = 99999;
        LectureApplyRequest request = createLectureApplyRequest(lectureKey, userId);

        // when // then
        assertThatThrownBy(() -> lectureService.lectureApply(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않은 사용자");
    }

    // 존재하지 않은 특강 유니크키로 특강을 신청하는 경우
    @DisplayName("존재하지 않은 특강 유니크키의 경우 예외가 발생한다.")
    @Test
    void emptyLectureApply() {
        // given
        User user = createUser("박지용");
        userRepository.save(user);

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        lectureRepository.save(lecture);

        LectureApplyRequest request = createLectureApplyRequest(999999, user.getKey());

        // when // then
        assertThatThrownBy(() -> lectureService.lectureApply(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않은 특강");
    }

    // 특강 신청 인원이 30명이 초과된 경우 실패
    @DisplayName("특강 신청 정원이 초과된 경우 예외가 발생한다.")
    @Test
    void mexCountExcess() {
        // given
        User user = createUser("박지용");
        userRepository.save(user);

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        lectureRepository.save(lecture);

        LectureApplyRequest request = createLectureApplyRequest(lecture.getKey(), user.getKey());

        // TODO: 요런 경우에는 어떤식으로 테스트를 하면 퍼포먼스가 빨라질지 질문! (Q&A)
        createMexCountApplication(lecture);

        // when // then
        assertThatThrownBy(() -> lectureService.lectureApply(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("특강 신청 정원 초과");
    }

    private void createMexCountApplication(Lecture lecture) {
        for (int i = 0; i < lecture.getMaxCount(); i++) {
            User user = createUser("테스트 유저");
            userRepository.save(user);

            Application application = createApplication(lecture, user);
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

    private User createUser(String name) {
        return User.builder()
                   .name(name)
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

    private Application createApplication(Lecture lecture, User user) {
        return Application.builder()
                          .lecture(lecture)
                          .user(user)
                          .regDate(LocalDateTime.now())
                          .build();
    }
}