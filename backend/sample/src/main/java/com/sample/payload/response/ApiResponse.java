package com.sample.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ApiResponse {

    @Schema( type = "boolean", example = "true", description="올바르게 로직을 처리했으면 True, 아니면 False를 반환합니다.")
    private boolean check;
    
    @Schema( type = "object", example = "information", description="restful의 정보를 감싸 표현합니다. object형식으로 표현합니다.")
    private Object information;
    
    public ApiResponse(){};

    @Builder
    public ApiResponse(boolean check, Object information) {
        this.check = check;
        this.information = information;
    }
}
