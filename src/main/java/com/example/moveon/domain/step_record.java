package com.example.moveon.domain;

import com.example.moveon.domain.common.BaseEntity;
import com.example.moveon.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigInteger;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@Entity // JPA의 엔티티
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class step_record extends BaseEntity {
    // 걸음수 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA가 통신하는 DBMS의 방식을 따른다. - Mysql
    private Long step_record_id;

    // 뛴 날짜
    private Date date;

    // 걸음 수
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer steps;

    //심박수
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer heart_rate;

    // 뛴 거리
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer distance;

    // 뛴 시간
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Time run_time;

    // 유저와의 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="record_user_id")
    private user user;

}
