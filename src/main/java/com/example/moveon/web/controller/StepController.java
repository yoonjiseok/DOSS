package com.example.moveon.web.controller;

import com.example.moveon.service.StepService;
import com.example.moveon.web.dto.Response;
import com.example.moveon.web.dto.UserDTO.UserStepInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "steps", description = "걸음수 API")
public class StepController {
    private final StepService stepService;

    @Operation(summary = "사용자 걸음수 저장", description = "id, 걸음수, 날짜를 저장한다")
    @PostMapping("/steps")
    public Response<?> postUserStep(
            @Parameter(description = "걸음수 저장 userId", required = true)
            @RequestBody UserStepInfo userStepInfo
    ) {
        stepService.postUserStep(userStepInfo);
        return Response.createSuccessWithNoData();
    }

    @Operation(summary = "사용자 걸음수 get", description = "날짜와 id에 맞는 걸음수를 불러온다")
    @Parameters({
            @Parameter(name = "user-id", description = "걸음수 불러오기 userId", required = true),
            @Parameter(name = "start-date", description = "시작 날짜", required = true),
            @Parameter(name = "end-date", description = "종료 날짜", required = true)
    })
    @GetMapping("/steps")
    public Response<List<Integer>> getUserStep(
            @RequestParam(name = "user-id") Long userId,
            @RequestParam(name = "start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(name = "end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        return Response.createSuccess(stepService.getUserStepWhileWeek(userId, startDate, endDate));
    }
}
