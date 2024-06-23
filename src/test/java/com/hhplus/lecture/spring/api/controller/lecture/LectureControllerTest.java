package com.hhplus.lecture.spring.api.controller.lecture;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.lecture.spring.api.controller.lecture.dto.common.LectureDTO;
import com.hhplus.lecture.spring.api.controller.lecture.dto.request.LectureApplyRequest;
import com.hhplus.lecture.spring.api.controller.lecture.dto.response.LectureResponse;
import com.hhplus.lecture.spring.api.service.LectureService;
import java.time.LocalDateTime;
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
        long lectureKey = 1;
        long userId = 1;

        LectureApplyRequest request = LectureApplyRequest.builder()
                                                         .lectureKey(lectureKey)
                                                         .userId(userId)
                                                         .build();

        LectureResponse lectureResponse = createLectureResponse(lectureKey);
        given(lectureService.lectureApply(request)).willReturn(lectureResponse);

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
                   jsonPath("$.data.lecture.key").value(userId),
                   jsonPath("$.data.lecture.title").value("허재 코치님의 특강"),
                   jsonPath("$.data.lecture.desc").value("무신사 29cm 전시 시스템 개발자 허재 코치님의 특강")
                   //jsonPath("$.data.lecture.startDate").value(LocalDateTime.of(2024, 4, 30, 13, 0, 0))
                );
    }

    private LectureResponse createLectureResponse(long lectureKey) {
        return new LectureResponse(LectureDTO.builder()
                                        .key(lectureKey)
                                        .title("허재 코치님의 특강")
                                        .desc("무신사 29cm 전시 시스템 개발자 허재 코치님의 특강")
                                        .startDate(LocalDateTime.of(2024, 4, 30, 13, 0, 0))
                                        .build());
    }

    @Test
    void getUserApplication() {
    }
}