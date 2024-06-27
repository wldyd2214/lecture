package com.hhplus.lecture.spring.api.controller.lecture.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "특상 신청 요청 DTO")
@AllArgsConstructor
@NoArgsConstructor
// 원인 : 해당 어노테이션이 없는 경우 mocking 된 service 에 스텁 메서드로 들어가는 Dto 인스턴스와
//       mockmvc.perform 할 때 생기는 Dto 인스턴스가 서로 달라 null 값이 반환 됨.
// 해결 : mockito 에서 스텁 매서드의 파라미터를 비교할 때 equals()를 통해서 비교하므로
//       @EqualsAndHashCode 어노테이션을 통해 eqauls()를 값을 비교하도록 오버라이딩하여 해결
@EqualsAndHashCode
@Getter
@Builder
public class LectureApplyRequest {

    @Schema(description = "특강 스케줄 유니크키")
    @Positive(message = "특강 스케줄 유니크키는 양수값만 입력할 수 있습니다.")
    private Long scheduleKey;

    @Schema(description = "신청자 유니크키")
    @Positive(message = "신청자 유니크키는 양수값만 입력할 수 있습니다.")
    private Long userId;
}
