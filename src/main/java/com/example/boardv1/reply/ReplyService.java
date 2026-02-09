package com.example.boardv1.reply;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.boardv1._core.errors.ex.Exception403;
import com.example.boardv1._core.errors.ex.Exception404;
import com.example.boardv1.board.Board;
import com.example.boardv1.user.User;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final EntityManager em;

    @Transactional
    public void 댓글작성(String comment, Integer boardId, Integer sessionUserId) {

        // 비영속객체체
        Board board = em.getReference(Board.class, boardId);
        User user = em.getReference(User.class, sessionUserId);
        Reply reply = new Reply();

        reply.setComment(comment);
        reply.setBoard(board);
        reply.setUser(user);

        // persist
        replyRepository.save(reply);
    }

    @Transactional
    public void 댓글삭제(Integer id, Integer sessionUserId) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new Exception404("댓글 찾을수 없어요"));

        if (sessionUserId != reply.getUser().getId())
            throw new Exception403("삭제할 권한이 없습니다.");

        replyRepository.delete(reply);

    }

}
