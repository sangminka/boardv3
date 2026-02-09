package com.example.boardv1.board;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import com.example.boardv1.reply.ReplyResponse;

import lombok.Data;

public class BoardResponse {

    @Data
    public static class DetailDTO {

        // 화면에 보이지 않는 것
        private Integer id;
        private Integer userId;

        // 화면에 보이는 것
        private String title;
        private String content; // 원본 (수정용)
        private String username;

        // 연산해서 만들어야 되는 것
        private boolean isOwner;
        private List<ReplyResponse.DTO> replies;

        public DetailDTO(Board board, Integer sessionUserId) {
            this.id = board.getId();
            this.userId = board.getUser().getId();
            this.title = board.getTitle();

            this.content = board.getContent();
            // this.content = Jsoup.clean(board.getContent(), Safelist.basic());

            this.username = board.getUser().getUsername();
            this.isOwner = board.getUser().getId().equals(sessionUserId);
            this.replies = board.getReplies().stream()
                    .map(reply -> new ReplyResponse.DTO(reply, sessionUserId))
                    .toList();
        }
    }

    @Data
    public static class IndexDTO {
        private Integer id;
        private String title;
        private String content;

        public IndexDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();

            // HTML -> 텍스트(태그 제거)
            String text = Jsoup.parse(board.getContent()).text().trim();

            // 길이 제한(원하는 길이로 조절)
            int max = 40;
            this.content = text.length() > max ? text.substring(0, max) + "..." : text;
        }
    }
}
