package com.hhplus.lecture.spring.api.controller.lecture.dto.response;

import com.hhplus.lecture.spring.api.controller.lecture.dto.common.LectureDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "특강 신청 목록 정보 DTO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LectureListResponseDTO {

    @Schema(description = "특강 목록 정보 DTO")
    private List<LectureDTO> lectures;

    public static LectureListResponseDTO of(List<LectureDTO> lectureDTOList) {
        return LectureListResponseDTO.builder()
                                     .lectures(lectureDTOList)
                                     .build();
    }
}
