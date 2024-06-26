package com.hhplus.lecture.spring.domain.schedule;

import com.hhplus.lecture.spring.domain.application.Application;
import com.hhplus.lecture.spring.domain.lecture.Lecture;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "TLS_MAX_COUNT", nullable = false)
    private Integer maxCount;

    @Column(name = "TLS_REG_DATE", nullable = false)
    private LocalDateTime regDate;

    @OneToMany(mappedBy = "lectureSchedule", cascade = CascadeType.ALL)
    private List<Application> applications = new ArrayList<>();

    @Builder
    public LectureSchedule(Lecture lecture, LocalDateTime date, Integer maxCount, LocalDateTime regDate) {
        this.lecture = lecture;
        this.date = date;
        this.maxCount = maxCount;
        this.regDate = regDate;
    }
}
