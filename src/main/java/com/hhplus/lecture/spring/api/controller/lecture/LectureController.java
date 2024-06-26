package com.hhplus.lecture.spring.api.controller.lecture;

import com.hhplus.lecture.spring.api.ApiResponse;
import com.hhplus.lecture.spring.api.controller.lecture.dto.request.LectureApplyRequest;
import com.hhplus.lecture.spring.api.controller.lecture.dto.response.LectureListResponse;
import com.hhplus.lecture.spring.api.controller.lecture.dto.response.LectureResponse;
import com.hhplus.lecture.spring.api.service.LectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Lecture", description = "Lecture 관련 API 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "lectures")
public class LectureController {

    private final LectureService lectureService;

    // 특강 신청 API (핵심)
    // POST /lectures/apply
    @Operation(
        summary = "특강 신청 API",
        description = "특강 신청을 합니다."
    )
    @PostMapping(value = "apply")
    public ApiResponse<LectureResponse> lectureApply(@RequestBody LectureApplyRequest request) {
        return ApiResponse.ok(lectureService.lectureApply(request));
    }

    // 특강 목록 API GET /lectures
    @Operation(
            summary = "특강 목록 API",
            description = "특강 목록을 조회 합니다."
    )
    @GetMapping(value = "")
    public ApiResponse<LectureListResponse> getLectures() {
        return ApiResponse.ok(lectureService.getLectures());
    }

    // 특강 신청 여부 조회 API
    // GET /lectures/application/{userId}
    @Operation(
        summary = "특강 신청 여부 조회 API",
        description = "특강 신청 여부를 조회 합니다."
    )
    @GetMapping(value = "application/{userId}")
    public ApiResponse<LectureListResponse> getUserApplication(@PathVariable long userId) {
        return ApiResponse.ok(lectureService.getUserApplication(userId));
    }
}
