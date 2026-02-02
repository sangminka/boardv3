package com.example.boardv1.user;

import lombok.Data;

public class UserRequset {

    @Data
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;
    }
}
