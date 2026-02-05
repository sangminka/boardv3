package com.example.boardv1.reply;

import lombok.Data;

public class ReplyRequest {

    @Data
    static class SaveDTO {
        private String comment;
        private Integer boardId;

    }

    @Data
    static class DeleteDTO {
        private Integer id;
        private Integer boardId;
    }

}
