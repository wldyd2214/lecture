package com.hhplus.lecture.spring.domain.application;

import com.hhplus.lecture.spring.domain.lecture.Lecture;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_APPLICATION_HISTORY")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAH_KEY", nullable = false)
    private Long key;

    private Long userId;

    // fetch = FetchType.LAZY (데이터가 꼭 필요한 시점에 쿼리가 되도록 설정)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TL_KEY", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Lecture lecture;

    @Column(name = "TAH_REG_DATE", nullable = false)
    private LocalDateTime regDate;

    @Builder
    public Application(Lecture lecture, Long userId, LocalDateTime regDate) {
        this.lecture = lecture;
        this.userId = userId;
        this.regDate = regDate;
    }
}
