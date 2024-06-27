package com.hhplus.lecture.spring.api.service;

import com.hhplus.lecture.spring.api.controller.lecture.dto.request.LectureApplyRequest;
import com.hhplus.lecture.spring.api.controller.lecture.dto.response.LectureResponse;
import com.hhplus.lecture.spring.domain.application.Application;
import com.hhplus.lecture.spring.domain.application.ApplicationRepository;
import com.hhplus.lecture.spring.domain.lecture.Lecture;
import com.hhplus.lecture.spring.domain.lecture.LectureRepository;
import com.hhplus.lecture.spring.domain.schedule.LectureSchedule;
import com.hhplus.lecture.spring.domain.schedule.LectureScheduleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private LectureScheduleRepository lectureScheduleRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @AfterEach
    void tearDown() {
        lectureRepository.deleteAllInBatch();
        lectureScheduleRepository.deleteAllInBatch();
        applicationRepository.deleteAllInBatch();
    }

    @DisplayName("존재하지 않은 특강 유니크키의 경우 예외가 발생한다.")
    @Test
    void emptyLectureApply() {
        // given
        long userId = 1;
        long lectureKey = 9999;
        long scheduleKey = 1;

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        lectureRepository.save(lecture);

        LectureApplyRequest request = createLectureApplyRequest(lectureKey, scheduleKey, userId);

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
        long scheduleKey = 1;

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        lectureRepository.save(lecture);

        LectureApplyRequest request = createLectureApplyRequest(lecture.getKey(), scheduleKey, userId);

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
        long scheduleKey = 1;

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        lectureRepository.save(lecture);
        //applicationRepository.save(createApplication(lecture, userId));

        LectureApplyRequest request = createLectureApplyRequest(lecture.getKey(), scheduleKey, userId);

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
        long scheduleKey = 1;

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        lectureRepository.save(lecture);

        LectureApplyRequest request = createLectureApplyRequest(lecture.getKey(), scheduleKey, userId);

        LectureResponse lectureResponse = lectureService.lectureApply(request);

        // when // then
        assertThat(lectureResponse.getLecture()).isNotNull();
        assertThat(lectureResponse.getLecture())
            .extracting("title", "desc")
            .contains(lecture.getTitle(), lecture.getDesc());
    }

    // TODO: 강의 신청 동시성 테스트 추가
    @DisplayName("강의 신청 동시성 테스트")
    @Test
    void lectureApplyAsync() throws InterruptedException {
        // given
        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        lectureRepository.save(lecture);

        LectureSchedule schedule = createLectureSchedule(lecture, 30, 29);
        lectureScheduleRepository.save(schedule);

        createApplicationBulk(schedule);

        long userId1 = 1;
        long userId2 = 2;
        long userId3 = 3;
        long userId4 = 4;
        long userId5 = 5;

        LectureApplyRequest request1 = createLectureApplyRequest(lecture.getKey(), schedule.getKey(), userId1);
        LectureApplyRequest request2 = createLectureApplyRequest(lecture.getKey(), schedule.getKey(), userId2);
        LectureApplyRequest request3 = createLectureApplyRequest(lecture.getKey(), schedule.getKey(), userId3);
        LectureApplyRequest request4 = createLectureApplyRequest(lecture.getKey(), schedule.getKey(), userId4);
        LectureApplyRequest request5 = createLectureApplyRequest(lecture.getKey(), schedule.getKey(), userId5);

        // when
        int count = 50;

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try{
                    LectureApplyRequest request = createLectureApplyRequest(lecture.getKey(), schedule.getKey(), finalI);
                    lectureService.lectureApply(request);
                }catch (Exception e){
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
//        CompletableFuture.allOf(
//                CompletableFuture.runAsync(() -> {
//                    lectureService.lectureApply(request1);
//                }),
//                CompletableFuture.runAsync(() -> {
//                    lectureService.lectureApply(request2);
//                }),
//                CompletableFuture.runAsync(() -> {
//                    lectureService.lectureApply(request3);
//                }),
//                CompletableFuture.runAsync(() -> {
//                    lectureService.lectureApply(request4);
//                }),
//                CompletableFuture.runAsync(() -> {
//                    lectureService.lectureApply(request5);
//                })
//        ).join();

        LectureSchedule currentCount = lectureScheduleRepository.findById(schedule.getKey()).orElseThrow();
        long historyCount = applicationRepository.countByLectureSchedule(schedule);

        List<Application> historyList = applicationRepository.findAll();

        historyList.stream().forEach(i -> System.out.println(i.getKey()));

        System.out.println("[현재 신청된 사용자 수] : " + currentCount.getCurrentCount());
        System.out.println("[현재 신청된 사용자 히스토리 수] : " + historyCount);

        assertThat(currentCount.getCurrentCount()).isEqualTo(30);
        assertThat(historyCount).isEqualTo(30);
    }

    private void createApplicationBulk(LectureSchedule schedule) {

        List<Application> applications = new ArrayList<>();

        for (int i = 0; i < 29; i++) {
            applications.add(createApplication(schedule, 9999 + i));
        }

        List<Application> saveApplications = applicationRepository.saveAll(applications);
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

    private LectureApplyRequest createLectureApplyRequest(long lectureKey, long scheduleKey, long userId) {
        return LectureApplyRequest.builder()
                                  .lectureKey(lectureKey)
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