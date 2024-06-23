package com.hhplus.lecture.spring.api.controller.lecture.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "특상 신청 요청 DTO")
@NoArgsConstructor
@Getter
public class LectureApplyRequest {

    @Schema(description = "특강 유니크키")
    @Positive(message = "특강 유니크키는 양수값만 입력할 수 있습니다.")
    private Long lectureKey;

    @Schema(description = "신청자 유니크키")
    @Positive(message = "신청자 유니크키는 양수값만 입력할 수 있습니다.")
    private Long userId;
}
