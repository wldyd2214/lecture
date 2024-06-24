package com.hhplus.lecture.spring.domain.lecture;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_LECTURE")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TL_KEY", nullable = false)
    private Long key;

    @Column(name = "TL_TITLE", nullable = false, length = 50)
    private String title;

    @Column(name = "TL_DESC", nullable = false, length = 300)
    private String desc;

    @Column(name = "TL_START_DATE", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "TL_MAX_COUNT", nullable = false)
    private Integer maxCount;

    @Builder
    public Lecture(String title, String desc, LocalDateTime startDate, Integer maxCount) {
        this.title = title;
        this.desc = desc;
        this.startDate = startDate;
        this.maxCount = maxCount;
    }
}
