package com.hhplus.lecture.spring.api.controller.lecture.dto.response;

import com.hhplus.lecture.spring.api.controller.lecture.dto.common.LectureDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "특강 신청 정보 DTO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LectureResponse {

    @Schema(description = "특강 정보 DTO")
    private LectureDTO lecture;

    public static LectureResponse of(LectureDTO dto) {
        return LectureResponse.builder()
                              .lecture(dto)
                              .build();

    }
}
