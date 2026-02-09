package com.example.boardv1.reply;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class ReplyRequest {

    @Data
    static class SaveDTO {
        @NotBlank(message = "내용은 필수입니다.")
        private String comment;
        private Integer boardId;

    }

    @Data
    static class DeleteDTO {
        private Integer id;
        private Integer boardId;
    }

}
