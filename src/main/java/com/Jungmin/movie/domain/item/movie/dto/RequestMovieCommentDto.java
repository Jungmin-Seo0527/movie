package com.Jungmin.movie.domain.item.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RequestMovieCommentDto {

    private Long userId;
    private Long movieId;
    private String contents;
}
