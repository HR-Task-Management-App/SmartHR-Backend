package com.hrms.backend.dtos.entityDtos.Comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {

    @NotNull
    @NotBlank
    private String taskId;

    @NotNull
    @NotBlank
    private String text;
}
