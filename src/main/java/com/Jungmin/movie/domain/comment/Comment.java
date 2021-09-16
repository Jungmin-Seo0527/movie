package com.Jungmin.movie.domain.comment;

import com.Jungmin.movie.domain.item.movie.Movie;
import com.Jungmin.movie.domain.post.Posts;
import com.Jungmin.movie.domain.user.User;
import lombok.AccessLevel;
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

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter(AccessLevel.PRIVATE) @Builder
@NoArgsConstructor @AllArgsConstructor
public class Comment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private User author;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private String content;
}
