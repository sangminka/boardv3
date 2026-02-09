package com.example.boardv1.reply;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.boardv1._core.errors.ex.Exception401;
import com.example.boardv1.user.User;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ReplyController {

    private final ReplyService replyService;
    private final HttpSession session;

    private User getSessionUser() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        return sessionUser;
    }

    @PostMapping("/replies/save")
    public String save(@Valid ReplyRequest.SaveDTO saveDto, Errors errors) {
        // 인증(v).권한(x)
        User sessionUser = getSessionUser();
        replyService.댓글작성(saveDto.getComment(), saveDto.getBoardId(), sessionUser.getId());

        return "redirect:/boards/" + saveDto.getBoardId();
    }

    @PostMapping("/replies/{id}/delete")
    public String delete(@PathVariable("id") Integer id, @RequestParam("boardId") Integer boardId) {
        User sessionUser = getSessionUser();
        replyService.댓글삭제(id, sessionUser.getId());

        return "redirect:/boards/" + boardId;
    }

}
