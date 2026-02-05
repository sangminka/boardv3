package com.example.boardv1.reply;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ReplyRepository {

    private final EntityManager em;

    public Reply save(Reply reply) {
        em.persist(reply);
        return reply;
    }

    public void delete(Reply reply) {
        em.remove(reply);
    }

    public Optional<Reply> findById(int id) {
        // select * from board_tb where id = 1;
        // ResultSet rs -> Board 객체 옮기기 (Object Mapping)
        // Board board = new Board();
        // board.id = rs.getInt("id");
        Reply reply = em.find(Reply.class, id);
        return Optional.ofNullable(reply);
    }

}
