package com.hhplus.lecture.spring.api.controller.lecture.dto.common;

import com.hhplus.lecture.spring.domain.application.Application;
import com.hhplus.lecture.spring.domain.schedule.LectureSchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public static LectureScheduleDTO toDto(LectureSchedule entity) {
        return LectureScheduleDTO.builder()
                                 .key(entity.getKey())
                                 .startDate(entity.getDate())
                                 .maxCount(entity.getMaxCount())
                                 .applications(entity.getApplications()
                                                     .stream()
                                                     .map(ApplicationDTO::toDto)
                                                     .collect(Collectors.toList()))
                                 .build();
    }
}
