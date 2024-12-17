package com.example.moveon.web.dto.StepDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(title = "개인 걸음수 정보")
public class user_stepdto {

    @Schema(description = "걸음수 기록 날짜", example = "2024-07-05", type = "string")
    private Date date;

    @Schema(description = "유저id", example = "3")
    private Long userId;

}