package com.example.moveon.exception;


import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 그룹명입니다."),
    DUPLICATED_NAME(HttpStatus.BAD_REQUEST, "중복된 닉네임입니다."),
    ALREADY_JOINED(HttpStatus.BAD_REQUEST, "이미 가입된 유저입니다."),
    ALREADY_SIGNED_OUT(HttpStatus.BAD_REQUEST, "이미 탈퇴된 유저입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    ACHIEVEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 업적입니다."),
    ACHIEVEMENT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 업적 카테고리 입니다."),
    COMMUNITY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 그룹입니다."),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 알림입니다."),
    PIXEL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 픽셀입니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이미지입니다."),
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "장소가 등록되어 있지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러 입니다"),
    VERSION_NOT_FOUND(HttpStatus.NOT_FOUND, "버전이 존재하지 않습니다."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "정상적인 이메일 요청이 아닙니다."),

    // 권한 관련 에러
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다"),

    // JWT 관련 에러
    JWT_NOT_EXISTS(HttpStatus.UNAUTHORIZED, "요청에 JWT가 존재하지 않습니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT입니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT가 만료되었습니다."),

    // 분산락 관련 에러
    LOCK_ACQUISITION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "락을 획득하는데 실패하였습니다.");

    // 랭킹 관련 에러

    private final HttpStatus httpStatus;
    private final String message;
}
