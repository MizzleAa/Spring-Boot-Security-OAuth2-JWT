package com.sample.advice.payload;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_PARAMETER(400, null, "잘못된 요청 데이터 입니다."),
    INVALID_REPRESENTATION(400, null, "잘못된 표현 입니다."),
    INVALID_FILE_PATH(400, null, "잘못된 파일 경로 입니다."),
    INVALID_OPTIONAL_ISPRESENT(400, null, "해당 값이 존재하지 않습니다."),
    INVALID_CHECK(400, null, "해당 값이 유효하지 않습니다."),
    INVALID_AUTHENTICATION(400, null, "잘못된 인증입니다.");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
