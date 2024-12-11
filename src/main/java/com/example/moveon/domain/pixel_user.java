package com.example.moveon.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity // JPA의 엔티티
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class pixel_user {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA가 통신하는 DBMS의 방식을 따른다. - Mysql
    private Long pixel_user_id;

    @Column(nullable = false)
    private Integer n_pixel;

    //pixel table과 pixel id로 매핑
    @OneToMany(mappedBy = "pixel_user", cascade = CascadeType.ALL)
    private List<pixel> pixel_user = new ArrayList<>();

    // user table 과 user id 로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user")
    private user user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="week_pixel")
    private week_pixel week_pixel;


}
