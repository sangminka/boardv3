package com.example.boardv1.user;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {
    // DI
    private final UserService userService;
    private final HttpSession session;

    // POST

    @PostMapping("/login")
    public String methodName(@Valid UserRequset.LoginDTO reqDto, HttpServletResponse resp, Errors errors) {
        User sessionUser = userService.로그인(reqDto);
        session.setAttribute("sessionUser", sessionUser);
        Cookie cookie = new Cookie("username", sessionUser.getUsername());
        cookie.setHttpOnly(false);
        resp.addCookie(cookie);

        return "redirect:/";
    }

    @PostMapping("/join")
    public String methodName(@Valid UserRequset.JoinDTO joinDTO, Errors errors) {

        userService.회원가입(joinDTO);
        return "redirect:/login-form";
    }

    // GET

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/join-form")
    public String joinForm() {
        return "/user/join-form";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "/user/login-form";
    }

}
