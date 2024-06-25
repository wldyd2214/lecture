package com.hhplus.lecture.spring.domain.schedule;

import com.hhplus.lecture.spring.domain.lecture.Lecture;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_LECTURE_SCHEDULE")
public class LectureSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TLS_KEY", nullable = false)
    private Long key;

    // fetch = FetchType.LAZY (데이터가 꼭 필요한 시점에 쿼리가 되도록 설정)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TL_KEY", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Lecture lecture;

    @Column(name = "TLS_DATE", nullable = false)
    private LocalDateTime date;

    @Column(name = "TLS_MEX_COUNT", nullable = false)
    private Integer maxCount;

    @Column(name = "TLS_REG_DATE", nullable = false)
    private LocalDateTime regDate;
}
