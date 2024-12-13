package com.example.moveon.web.dto.PixelDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "개인전 픽셀 정보")
public interface IndividualModePixelResponse {
    @Schema(description = "픽셀 ID", example = "78611")
    Long getPixelId();

    @Schema(description = "픽셀 좌측 상단 위도", example = "37.503717")
    double getLatitude();

    @Schema(description = "픽셀 좌측 상단 경도", example = "127.044317")
    double getLongitude();

    @Schema(description = "소유주의 ID", example = "3")
    Long getUserId();

    @Schema(description = "픽셀 세로 상대 좌표", example = "224")
    Integer getX();

    @Schema(description = "픽셀 가로 상대 좌표", example = "210")
    Integer getY();
}
