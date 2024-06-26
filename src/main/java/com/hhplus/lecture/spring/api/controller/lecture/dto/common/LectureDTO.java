package com.hhplus.lecture.spring.api.controller.lecture.dto.common;

import com.hhplus.lecture.spring.domain.schedule.LectureSchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import java.util.stream.Collectors;
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

    public static LectureDTO createLectureDTO(long key, String title, String desc, List<LectureSchedule> schedules) {
        return LectureDTO.builder()
                         .key(key)
                         .title(title)
                         .desc(desc)
                         .schedules(schedules.stream()
                                             .map(LectureScheduleDTO::toDto)
                                             .collect(Collectors.toList()))
                         .build();
    }
}
