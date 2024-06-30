package com.hhplus.lecture.spring.api.service;

import com.hhplus.lecture.spring.api.controller.lecture.dto.request.LectureApplyRequest;
import com.hhplus.lecture.spring.domain.application.Application;
import com.hhplus.lecture.spring.domain.application.ApplicationRepository;
import com.hhplus.lecture.spring.domain.lecture.Lecture;
import com.hhplus.lecture.spring.domain.lecture.LectureRepository;
import com.hhplus.lecture.spring.domain.schedule.LectureSchedule;
import com.hhplus.lecture.spring.domain.schedule.LectureScheduleRepository;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class LectureServiceIntegrationTest {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LectureScheduleRepository lectureScheduleRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @AfterEach
    void tearDown() {
        lectureRepository.deleteAllInBatch();
        lectureScheduleRepository.deleteAllInBatch();
        applicationRepository.deleteAllInBatch();
    }

    @DisplayName("강의 신청 동시성 테스트")
    @Test
    void lectureApplyAsync() throws InterruptedException {
        // given
        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        lectureRepository.save(lecture);

        int mexPeopleCount = 30;
        int currentPeopleCount = 29;
        LectureSchedule schedule = createLectureSchedule(lecture, mexPeopleCount, currentPeopleCount);
        lectureScheduleRepository.save(schedule);

        createApplicationBulk(schedule, 29);

        // anyOf
        CompletableFuture.allOf(
            CompletableFuture.runAsync(() -> lectureService.lectureApply(schedule.getKey(), 1))
                             .handle((result, ex) -> {
                                 if (ex != null) System.out.println("1 수강 신청 실패!");
                                 return "";
                             }),

            CompletableFuture.runAsync(() -> lectureService.lectureApply(schedule.getKey(), 2))
                             .handle((result, ex) -> {
                                 if (ex != null) System.out.println("2 수강 신청 실패!");
                                 return "";
                             }),
            CompletableFuture.runAsync(() -> lectureService.lectureApply(schedule.getKey(), 3))
                             .handle((result, ex) -> {
                                 if (ex != null) System.out.println("3 수강 신청 실패!");
                                 return "";
                             }),
            CompletableFuture.runAsync(() -> lectureService.lectureApply(schedule.getKey(), 4))
                             .handle((result, ex) -> {
                                 if (ex != null) System.out.println("4 수강 신청 실패!");
                                 return "";
                             }),
            CompletableFuture.runAsync(() -> lectureService.lectureApply(schedule.getKey(), 5))
                             .handle((result, ex) -> {
                                 if (ex != null) System.out.println("5 수강 신청 실패!");
                                 return "";
                             })
        ).join();

        LectureSchedule currentCount = lectureScheduleRepository.findById(schedule.getKey()).orElseThrow();
        long historyCount = applicationRepository.countByLectureSchedule(schedule);

        System.out.println("[현재 신청된 사용자 수] : " + currentCount.getCurrentCount());
        System.out.println("[현재 신청된 사용자 히스토리 수] : " + historyCount);

        assertThat(currentCount.getCurrentCount()).isEqualTo(mexPeopleCount);
        assertThat(historyCount).isEqualTo(mexPeopleCount);
    }

    private void createApplicationBulk(LectureSchedule schedule, int maxApplicationCount) {

        List<Application> applications = new ArrayList<>();

        for (int i = 0; i < maxApplicationCount; i++) {
            applications.add(createApplication(schedule, 9999 + i));
        }

        applicationRepository.saveAll(applications);
    }

    private LectureSchedule createLectureSchedule(Lecture lecture, Integer maxCount, Integer currentCount) {
        return LectureSchedule.builder()
                              .lecture(lecture)
                              .date(LocalDateTime.now())
                              .maxCount(maxCount)
                              .currentCount(currentCount)
                              .regDate(LocalDateTime.now())
                              .build();
    }

    private LectureApplyRequest createLectureApplyRequest(long scheduleKey, long userId) {
        return LectureApplyRequest.builder()
                                  .scheduleKey(scheduleKey)
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

    private Application createApplication(LectureSchedule schedule, long userId) {
        return Application.builder()
                          .userId(userId)
                          .lectureSchedule(schedule)
                          .regDate(LocalDateTime.now())
                          .build();
    }
}