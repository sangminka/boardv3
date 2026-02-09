package com.example.boardv1._core.errors.ex;

// 유효성 검사실패시 사용 // 중복복
public class Exception400 extends RuntimeException {

    public Exception400(String message) {
        super(message);
    }

}
