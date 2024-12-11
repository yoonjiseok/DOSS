package com.example.moveon.web.controller;

import com.example.moveon.apipayload.ApiResponse;
import com.example.moveon.converter.MainPixelConverter;
import com.example.moveon.domain.step_record;
import com.example.moveon.service.MainService.MainCommandService;
import com.example.moveon.web.dto.PixelDTO.MainPixelRequestdto;
import com.example.moveon.web.dto.PixelDTO.MainPixelResponsedto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api-docs")
public class MainController {

    private final MainCommandService mainCommandService;

    @PostMapping("/main")
    public ApiResponse<MainPixelResponsedto.MainPageListDTO> join(@RequestBody @Valid MainPixelRequestdto.JoinDto request){
        step_record stepRecord = mainCommandService.addrecord(request);
        return ApiResponse.onSuccess(MainPixelConverter.toJoinResultDTO(stepRecord));
    }

    // 유저의 메인 pixel 수정 api (추가).
//    @PatchMapping("/{userId}/main")
//    @Operation(summary = "특정 유저의의 메인화면에서 픽셀 조회 API",description = "특정 유저의 메인 화면에서의 픽셀 갯수와 심박수 등을 조회하는 api 입니다.")
//    @ApiResponses({
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//    })
//    @Parameters({
//            @Parameter(name = "userId", description = "유저의 아이디, path variable 입니다!")
//    })
//    public ApiResponse<MainPixelResponsedto> getStoreMissionList(
//            @ExistStore @PathVariable(name = "storeId") Integer storeId,
//            @Parameter(description = "조회할 페이지 번호 (1 이상)", example = "1")
//            @CheckPage @RequestParam(name = "page") Integer page
//    ){
//        return ApiResponse.onSuccess(MainPixelConverter.missionPreViewListDTO(missionQueryServiceImpl.getStoreMissionList(storeId,page-1)));
//    }

}
