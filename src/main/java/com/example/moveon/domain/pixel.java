package com.example.moveon.domain;


import com.example.moveon.domain.common.BaseEntity;
import com.example.moveon.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity // JPA의 엔티티
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class pixel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA가 통신하는 DBMS의 방식을 따른다. - Mysql
    private Long id;

    @Column(nullable = false, length = 20) // 널 값 허용하지 않음
    private Date date;

    @Column(nullable = false, length = 40)
    private double x;

    @Column(nullable = false, length = 40)
    private double y;

//    // user table과 user_id 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user")
    private user user;

//    // pixel table과 pixel id 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="pixel_user")
    private pixel_user pixel_user;

}
