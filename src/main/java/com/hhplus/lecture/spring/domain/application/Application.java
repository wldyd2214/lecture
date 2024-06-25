package com.hhplus.lecture.spring.domain.application;

import com.hhplus.lecture.spring.domain.schedule.LectureSchedule;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_APPLICATION")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TA_KEY", nullable = false)
    private Long key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TLS_KEY", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private LectureSchedule lectureSchedule;

    @Column(name = "TAH_USER_ID", nullable = false)
    private Long userId;

    @Column(name = "TAH_REG_DATE", nullable = false)
    private LocalDateTime regDate;

    @Builder
    public Application(LectureSchedule lectureSchedule, Long userId, LocalDateTime regDate) {
        this.lectureSchedule = lectureSchedule;
        this.userId = userId;
        this.regDate = regDate;
    }
}
