package com.example.moveon.web.dto.UserDTO;

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
public class UserStepInfo {

    @Schema(description = "걸음수 기록 날짜", example = "2024-07-05", type = "string")
    private Date date;

    @Schema(description = "걸음수", example = "1557")
    private Integer steps;

    @Schema(description = "뛴 거리", example = "50")
    private Integer distance;

    @Schema(description = "심박수", example = "1557")
    private Integer HRate;

    @Schema(description = "달린 시간", example = "00:12:23" , type = "string")
    private Time run_time;

    @Schema(description = "메모", example = "오늘 하루 오운완")
    private String memo;

    @Schema(description = "유저id", example = "3")
    private Long userId;

}
