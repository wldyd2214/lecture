package com.hhplus.lecture.spring.api.controller.lecture.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LectureDTO {

    @Schema(description = "특강 유니크키")
    private Long key;

    @Schema(description = "특강 이름")
    private String title;

    @Schema(description = "특강 내용")
    private String desc;

    @Schema(description = "특강 스케줄 정보")
    private List<LectureScheduleDTO> schedules;
}
