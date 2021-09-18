package com.Jungmin.movie.domain.post;

import com.Jungmin.movie.domain.item.movie.Movie;
import com.Jungmin.movie.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;

@Getter @Setter(PRIVATE) @Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Posts {

    @Id @GeneratedValue
    @Column(name = "posts_id")
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String content;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

}
