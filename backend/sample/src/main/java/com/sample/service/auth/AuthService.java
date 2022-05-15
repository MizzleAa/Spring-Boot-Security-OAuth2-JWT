package com.sample.service.auth;

import java.net.URI;
import java.util.Optional;

import com.sample.advice.assertThat.DefaultAssert;
import com.sample.config.security.token.UserPrincipal;

import com.sample.domain.entity.user.Provider;
import com.sample.domain.entity.user.Role;
import com.sample.domain.entity.user.Token;
import com.sample.domain.entity.user.User;
import com.sample.domain.mapping.TokenMapping;
import com.sample.payload.request.auth.ChangePasswordRequest;
import com.sample.payload.request.auth.SignInRequest;
import com.sample.payload.request.auth.SignUpRequest;
import com.sample.payload.request.auth.RefreshTokenRequest;
import com.sample.payload.response.ApiResponse;
import com.sample.payload.response.AuthResponse;
import com.sample.payload.response.Message;
import com.sample.repository.auth.TokenRepository;
import com.sample.repository.user.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CustomTokenProviderService customTokenProviderService;
    
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    

    public ResponseEntity<?> whoAmI(UserPrincipal userPrincipal){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(user);
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(user.get()).build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> delete(UserPrincipal userPrincipal){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Token> token = tokenRepository.findByUserEmail(user.get().getEmail());
        DefaultAssert.isTrue(token.isPresent(), "토큰이 유효하지 않습니다.");

        userRepository.delete(user.get());
        tokenRepository.delete(token.get());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("회원 탈퇴하셨습니다.").build()).build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> modify(UserPrincipal userPrincipal, ChangePasswordRequest passwordChangeRequest){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        boolean passwordCheck = passwordEncoder.matches(passwordChangeRequest.getOldPassword(),user.get().getPassword());
        DefaultAssert.isTrue(passwordCheck, "잘못된 비밀번호 입니다.");

        boolean newPasswordCheck = passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getReNewPassword());
        DefaultAssert.isTrue(newPasswordCheck, "신규 등록 비밀번호 값이 일치하지 않습니다.");


        passwordEncoder.encode(passwordChangeRequest.getNewPassword());

        return ResponseEntity.ok(true);
    }

    public ResponseEntity<?> signin(SignInRequest signInRequest){
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                signInRequest.getEmail(),
                signInRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);
        Token token = Token.builder()
                            .refreshToken(tokenMapping.getRefreshToken())
                            .userEmail(tokenMapping.getUserEmail())
                            .build();
        tokenRepository.save(token);
        AuthResponse authResponse = AuthResponse.builder().accessToken(tokenMapping.getAccessToken()).refreshToken(token.getRefreshToken()).build();
        
        return ResponseEntity.ok(authResponse);
    }

    public ResponseEntity<?> signup(SignUpRequest signUpRequest){
        DefaultAssert.isTrue(!userRepository.existsByEmail(signUpRequest.getEmail()), "해당 이메일이 존재하지 않습니다.");

        User user = User.builder()
                        .name(signUpRequest.getName())
                        .email(signUpRequest.getEmail())
                        .password(passwordEncoder.encode(signUpRequest.getPassword()))
                        .provider(Provider.local)
                        .role(Role.ADMIN)
                        .build();

        userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/auth/")
                .buildAndExpand(user.getId()).toUri();
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("회원가입에 성공하였습니다.").build()).build();

        return ResponseEntity.created(location).body(apiResponse);
    }

    public ResponseEntity<?> refresh(RefreshTokenRequest tokenRefreshRequest){
        //1차 검증
        boolean checkValid = valid(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isAuthentication(checkValid);

        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());

        //4. refresh token 정보 값을 업데이트 한다.
        //시간 유효성 확인
        TokenMapping tokenMapping;

        Long expirationTime = customTokenProviderService.getExpiration(tokenRefreshRequest.getRefreshToken());
        if(expirationTime > 0){
            tokenMapping = customTokenProviderService.refreshToken(authentication, token.get().getRefreshToken());
        }else{
            tokenMapping = customTokenProviderService.createToken(authentication);
        }

        Token updateToken = token.get().updateRefreshToken(tokenMapping.getRefreshToken());
        tokenRepository.save(updateToken);

        AuthResponse authResponse = AuthResponse.builder().accessToken(tokenMapping.getAccessToken()).refreshToken(updateToken.getRefreshToken()).build();

        return ResponseEntity.ok(authResponse);
    }

    public ResponseEntity<?> signout(RefreshTokenRequest tokenRefreshRequest){
        boolean checkValid = valid(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isAuthentication(checkValid);

        //4 token 정보를 삭제한다.
        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        tokenRepository.delete(token.get());
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("로그아웃 하였습니다.").build()).build();

        return ResponseEntity.ok(apiResponse);
    }

    private boolean valid(String refreshToken){

        //1. 토큰 형식 물리적 검증
        boolean validateCheck = customTokenProviderService.validateToken(refreshToken);
        DefaultAssert.isTrue(validateCheck, "Token 검증에 실패하였습니다.");

        //2. refresh token 값을 불러온다.
        Optional<Token> token = tokenRepository.findByRefreshToken(refreshToken);
        DefaultAssert.isTrue(token.isPresent(), "탈퇴 처리된 회원입니다.");

        //3. email 값을 통해 인증값을 불러온다
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());
        DefaultAssert.isTrue(token.get().getUserEmail().equals(authentication.getName()), "사용자 인증에 실패하였습니다.");

        return true;
    }


}
