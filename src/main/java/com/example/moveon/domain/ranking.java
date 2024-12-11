package com.example.moveon.domain;

import com.example.moveon.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigInteger;
@Entity // JPA의 엔티티
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ranking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ranking_id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ranking_user_id", nullable = false)
    private user user;


}
