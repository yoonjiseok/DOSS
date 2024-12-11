package com.example.moveon.domain;

import com.example.moveon.domain.common.BaseEntity;
import com.example.moveon.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


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
public class user extends BaseEntity {

    // 유저 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA가 통신하는 DBMS의 방식을 따른다. - Mysql
    private Long id;

    // 유저 이름
    @Column(nullable = false, length = 20) // 널 값 허용하지 않음
    private String name;

    // 유저 주소
    @Column(nullable = false, length = 40)
    private String address;

    // 유저 상세 주소
    @Column
    private String specAddress;

    // 유저 성별
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private Gender gender;

    // 유저 email
    private String email;

    //  유저 나이
    private Integer age;

    // step_record 와 매핑
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<step_record> record_user_id = new ArrayList<>();

    // ranking 와 매핑
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ranking> ranking_user_id = new ArrayList<>();
}