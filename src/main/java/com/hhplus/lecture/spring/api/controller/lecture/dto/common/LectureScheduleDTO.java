package com.hhplus.lecture.spring.api.controller.lecture.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class LectureScheduleDTO {

    @Schema(description = "특강 스케줄 유니크키")
    private Long key;

    @Schema(description = "특강 날짜")
    private LocalDateTime startDate;

    @Schema(description = "특강 최대 인원수")
    private Integer maxCount;

    @Schema(description = "특강 신청 정보")
    private List<ApplicationDTO> applications;
}
