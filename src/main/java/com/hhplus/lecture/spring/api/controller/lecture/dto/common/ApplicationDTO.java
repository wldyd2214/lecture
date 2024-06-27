package com.hhplus.lecture.spring.api.controller.lecture.dto.common;

import com.hhplus.lecture.spring.domain.application.Application;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicationDTO {

    @Schema(description = "특강 신청 유니크키")
    private Long key;

    @Schema(description = "특강 신청 사용자 아이디")
    private Long userId;

    @Schema(description = "특강 신청 일자")
    private LocalDateTime regDate;

    public static ApplicationDTO toDto(Application entity) {
        return ApplicationDTO.builder()
                             .key(entity.getKey())
                             .userId(entity.getUserId())
                             .regDate(entity.getRegDate())
                             .build();
    }

}
