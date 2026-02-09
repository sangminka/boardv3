package com.example.boardv1.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class UserRequset {

    @Data
    public static class LoginDTO {
        @NotBlank(message = "유저네임을 입력해주세요")
        private String username;
        @NotBlank(message = "비밀번호를 입력해주세요")
        private String password;
    }

    @Data
    public static class JoinDTO {
        @NotBlank(message = "유저네임을 입력해주세요")
        @Size(min = 3, max = 20, message = "유저네임은 3~20자로 입력해주세요")
        private String username;
        @NotBlank(message = "비밀번호를 입력해주세요")
        @Size(min = 4, max = 20, message = "비밀번호는 4~20자로 입력해주세요")
        private String password;
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;
    }
}
