package com.hhplus.lecture.spring.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TU_KEY", nullable = false)
    private Long key;

    @Column(name = "TU_NAME", nullable = false, length = 50)
    private String name;

    @Builder
    public User(Long key, String name) {
        this.key = key;
        this.name = name;
    }
}
