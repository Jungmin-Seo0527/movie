package com.Jungmin.movie.domain.post;

import com.Jungmin.movie.domain.comment.Comment;
import com.Jungmin.movie.domain.item.movie.Movie;
import com.Jungmin.movie.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Getter @Setter(PRIVATE)
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Posts {

    @Id @GeneratedValue
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

    @Builder.Default
    @OneToMany(mappedBy = "posts", cascade = ALL)
    private List<Comment> commentList = new ArrayList<>();
}
