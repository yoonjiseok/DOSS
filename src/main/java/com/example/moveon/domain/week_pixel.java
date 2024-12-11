package com.example.moveon.domain;

import com.example.moveon.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class week_pixel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA가 통신하는 DBMS의 방식을 따른다. - Mysql
    private Long week_PXid;

    //pixel_user와 pixel_user_id 로 매핑
    @OneToMany(mappedBy = "week_pixel", cascade = CascadeType.ALL)
    private List<pixel_user> week_pixel_id = new ArrayList<>();

}
