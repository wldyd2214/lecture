package com.hhplus.lecture.spring.domain.application;

import com.hhplus.lecture.spring.domain.lecture.Lecture;
import com.hhplus.lecture.spring.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    // fetch = FetchType.LAZY (데이터가 꼭 필요한 시점에 쿼리가 되도록 설정)
    @ManyToOne(fetch = FetchType.LAZY)
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "TAH_REG_DATE", nullable = false)
    private LocalDateTime regDate;

    @Builder
    public Application(Long key, Lecture lecture, User user, LocalDateTime regDate) {
        this.key = key;
        this.lecture = lecture;
        this.user = user;
        this.regDate = regDate;
    }
}
