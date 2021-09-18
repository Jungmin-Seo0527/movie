package com.Jungmin.movie.domain.comment;

import com.Jungmin.movie.domain.item.movie.Movie;
import com.Jungmin.movie.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter @Setter(PRIVATE) @Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class Comment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private User author;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private String content;

    public static Comment createComment(User user, Movie movie, String content) {
        Comment comment = Comment.builder()
                .author(user)
                .movie(movie)
                .content(content)
                .build();
        user.writeComment(comment);
        movie.writeComment(comment);
        return comment;
    }
}
