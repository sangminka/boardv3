package com.example.boardv1.reply;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.boardv1.board.Board;
import com.example.boardv1.user.User;

import jakarta.persistence.EntityManager;

@Import(ReplyRepositoryTest.class)
@DataJpaTest // EntityManager가 ioc에 등록됨
public class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private EntityManager em;

    @Test
    public void save_test() {
        // given
        User user = new User();
        user.setUsername("ssar");
        user.setPassword("1234");
        user.setEmail("ssar@test.com");

        em.persist(user); // ⭐ 반드시 먼저

        Board board = new Board();
        board.setTitle("제목");
        board.setContent("내용");
        board.setUser(user);

        em.persist(board); // ⭐ 반드시 먼저

        Reply reply = new Reply();
        reply.setComment("Zzz");
        reply.setUser(user);
        reply.setBoard(board);

        // when
        Reply savedReply = replyRepository.save(reply);
        em.flush();
        em.clear();

    }

}
