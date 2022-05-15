package com.sample.payload.request.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignInRequest {

    @Schema( type = "string", example = "string@aa.bb", description="계정 이메일 입니다.")
    @NotBlank
    @NotNull
    @Email
    private String email;

    @Schema( type = "string", example = "string", description="계정 비밀번호 입니다.")
    @NotBlank
    @NotNull
    private String password;
}
