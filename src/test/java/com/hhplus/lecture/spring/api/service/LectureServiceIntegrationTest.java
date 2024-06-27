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

    @DisplayName("특강 신청 정원이 초과된 경우 예외가 발생한다.")
    @Test
    void maxCountExcess() {
        // given
        long userId = 1;
        long scheduleKey = 1;
        int mexPeopleCount = 30;
        int currentPeopleCount = 30;

        LectureApplyRequest request = createLectureApplyRequest(scheduleKey, userId);

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        lectureRepository.save(lecture);

        LectureSchedule schedule = createLectureSchedule(lecture, mexPeopleCount, currentPeopleCount);
        lectureScheduleRepository.save(schedule);

        createApplicationBulk(schedule, currentPeopleCount);

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
        long scheduleKey = 1;
        int mexPeopleCount = 30;
        int currentPeopleCount = 1;

        LectureApplyRequest request = createLectureApplyRequest(scheduleKey, userId);

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        lectureRepository.save(lecture);

        LectureSchedule schedule = createLectureSchedule(lecture, mexPeopleCount, currentPeopleCount);
        lectureScheduleRepository.save(schedule);

        Application application = createApplication(schedule, userId);
        applicationRepository.save(application);

        // when // then
        assertThatThrownBy(() -> lectureService.lectureApply(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 신청한 특강 스케줄");
    }

    @DisplayName("특강 신청에 성공 후 특강 신청 정보를 반환한다.")
    @Test
    void lectureApply() {
        // given
        long userId = 1;
        long scheduleKey = 1;
        int mexPeopleCount = 30;
        int currentPeopleCount = 1;

        Lecture lecture = createLecture("김종협 코치님의 특강", "SIR.LOIN 테크팀 리드 김종협 코치님의 특강");
        lectureRepository.save(lecture);

        LectureSchedule schedule = createLectureSchedule(lecture, mexPeopleCount, currentPeopleCount);
        lectureScheduleRepository.save(schedule);

        LectureApplyRequest request = createLectureApplyRequest(scheduleKey, userId);

        LectureResponse lectureResponse = lectureService.lectureApply(request);

        // when // then
        assertThat(lectureResponse.getLecture()).isNotNull();
        assertThat(lectureResponse.getLecture())
            .extracting("title", "desc")
            .contains(lecture.getTitle(), lecture.getDesc());
    }

    // 동시성에 대한 테스트는 lectureApplyAsync를 통해 테스트하였습니다.
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
        lectureApplyAsyncProcess(schedule);

        LectureSchedule currentCount = lectureScheduleRepository.findById(schedule.getKey()).orElseThrow();
        long historyCount = applicationRepository.countByLectureSchedule(schedule);

        System.out.println("[현재 신청된 사용자 수] : " + currentCount.getCurrentCount());
        System.out.println("[현재 신청된 사용자 히스토리 수] : " + historyCount);

        assertThat(currentCount.getCurrentCount()).isEqualTo(mexPeopleCount);
        assertThat(historyCount).isEqualTo(mexPeopleCount);
    }

    private void lectureApplyAsyncProcess(LectureSchedule schedule) throws InterruptedException {
        // when
        int count = 50;

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try{
                    LectureApplyRequest request = createLectureApplyRequest(schedule.getKey(), finalI);
                    lectureService.lectureApply(request);
                }catch (Exception e){
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
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