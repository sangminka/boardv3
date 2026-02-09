package com.example.boardv1._core.errors.ex;

// 403 권한 실패시시
public class Exception403 extends RuntimeException {

    public Exception403(String message) {
        super(message);
    }

}
