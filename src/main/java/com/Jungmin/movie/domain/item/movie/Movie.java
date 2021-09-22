package com.Jungmin.movie.domain.item.movie;

import com.Jungmin.movie.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter @Setter(PRIVATE) @Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class Movie {

    @Id @GeneratedValue
    @Column(name = "movie_id")
    private Long id;

    private String title;

    private int price;

    private String genre;

    private String url;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "movie")
    private List<Comment> comments = new ArrayList<>();

    public void writeComment(Comment comment) {
        comments.add(comment);
    }

    public void pricingChange(int price) {
        setPrice(price);
    }
}
