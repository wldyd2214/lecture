package com.hhplus.lecture.spring.api.controller.lecture.dto.response;

import com.hhplus.lecture.spring.domain.lecture.LectureDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "특강 신청 목록 정보 DTO")
@NoArgsConstructor
@Getter
public class LectureListResponse {

    @Schema(description = "특강 목록 정보 DTO")
    private List<LectureDTO> lectures;
}
