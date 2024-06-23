package com.hhplus.lecture.spring.api.controller.lecture.dto.response;

import com.hhplus.lecture.spring.domain.lecture.LectureDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "특강 신청 정보 DTO")
@NoArgsConstructor
@Getter
public class LectureResponse {

    @Schema(description = "특강 정보 DTO")
    private LectureDTO lecture;
}
