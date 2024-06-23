package com.hhplus.lecture.spring.domain.lecture;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class LectureDTO {

    @Schema(description = "특강 유니크키")
    private Long key;

    @Schema(description = "특강 이름")
    private String title;

    @Schema(description = "특강 내용")
    private Long description;

    @Schema(description = "특강 시작일")
    private LocalDateTime startDate;
}
