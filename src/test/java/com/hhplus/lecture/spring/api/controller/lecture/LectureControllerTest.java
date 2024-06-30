package com.hhplus.lecture.spring.api.controller.lecture;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.lecture.spring.api.controller.lecture.dto.common.LectureDTO;
import com.hhplus.lecture.spring.api.controller.lecture.dto.request.LectureApplyRequest;
import com.hhplus.lecture.spring.api.controller.lecture.dto.response.LectureApplyResponseDTO;
import com.hhplus.lecture.spring.api.service.LectureService;
import com.hhplus.lecture.spring.domain.lecture.Lecture;
import com.hhplus.lecture.spring.domain.schedule.LectureSchedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LectureController.class)
class LectureControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected LectureService lectureService;

    @DisplayName("특강 신청을 성공한다.")
    @Test
    void lectureApply() throws Exception {
        // given
        long scheduleKey = 1;
        long userId = 1;

        LectureApplyRequest request = LectureApplyRequest.builder()
                                                         .scheduleKey(scheduleKey)
                                                         .userId(userId)
                                                         .build();

        String title = "허재 코치님의 특강";
        String desc = "무신사 29cm 전시 시스템 개발자 허재 코치님의 특강";

        given(lectureService.lectureApply(request.getScheduleKey(), request.getUserId()))
            .willReturn(createLectureSchedule(title, desc));

        // when // then
        mockMvc.perform(
                    post("/lectures/apply")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpectAll(
                   status().isOk(),
                   jsonPath("$.code").value("200"),
                   jsonPath("$.status").value("OK"),
                   jsonPath("$.message").value("OK"),
                   jsonPath("$.data.lecture.title").value(title),
                   jsonPath("$.data.lecture.desc").value(desc)
                );
    }

    private Lecture createLecture(String title, String desc) {
        return Lecture.builder()
                      .title(title)
                      .desc(desc)
                      .build();
    }

    private LectureSchedule createLectureSchedule(String title, String desc) {
        return LectureSchedule.builder()
                              .lecture(createLecture(title, desc))
                              .build();
    }

    @DisplayName("특강 신청시 강의 스케줄 유니크키가 음수인 경우 예외 처리를 확인한다.")
    @Test
    void lectureApplyPositiveScheduleKey() throws Exception {
        // given
        long scheduleKey = -1;
        long userId = 1;

        LectureApplyRequest request = LectureApplyRequest.builder()
                                                         .scheduleKey(scheduleKey)
                                                         .userId(userId)
                                                         .build();

        // when // then
        mockMvc.perform(
                   post("/lectures/apply")
                       .content(objectMapper.writeValueAsString(request))
                       .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpectAll(
                   status().isBadRequest(),
                   jsonPath("$.code").value("400"),
                   jsonPath("$.status").value("BAD_REQUEST"),
                   jsonPath("$.message").value("특강 스케줄 유니크키는 양수값만 입력할 수 있습니다.")
               );
    }

    @DisplayName("특강 신청시 강의 유니크키가 음수인 경우 예외 처리를 확인한다.")
    @Test
    void lectureApplyPositiveUserIdKey() throws Exception {
        // given
        long scheduleKey = 1;
        long userId = -1;

        LectureApplyRequest request = LectureApplyRequest.builder()
                                                         .scheduleKey(scheduleKey)
                                                         .userId(userId)
                                                         .build();

        // when // then
        mockMvc.perform(
                   post("/lectures/apply")
                       .content(objectMapper.writeValueAsString(request))
                       .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpectAll(
                   status().isBadRequest(),
                   jsonPath("$.code").value("400"),
                   jsonPath("$.status").value("BAD_REQUEST"),
                   jsonPath("$.message").value("신청자 유니크키는 양수값만 입력할 수 있습니다.")
               );
    }

    @DisplayName("특강 목록을 조회한다.")
    @Test
    void getLectures() throws Exception {
        // when // then
        mockMvc.perform(
                   get("/lectures")
                       .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpectAll(
                   status().isOk(),
                   jsonPath("$.code").value("200"),
                   jsonPath("$.status").value("OK"),
                   jsonPath("$.message").value("OK")
               );
    }

    @DisplayName("특강 신청 여부를 조회한다.")
    @Test
    void getUserApplication() throws Exception {
        long userId = 1;

        // when // then
        mockMvc.perform(
                   get("/lectures/application/{userId}".replace("{userId}", String.valueOf(userId)))
                       .contentType(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpectAll(
                   status().isOk(),
                   jsonPath("$.code").value("200"),
                   jsonPath("$.status").value("OK"),
                   jsonPath("$.message").value("OK")
               );
    }
}