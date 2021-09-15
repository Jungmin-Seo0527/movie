package com.Jungmin.movie.domain.item.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter @Setter(PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularMovie {

    @Id @GeneratedValue
    @Column(name = "movie_id")
    private Long id;

    private String title;

    private int rank;

    private int price;

    private String genre;

    private String url;

    private float star;

    @Enumerated(EnumType.STRING)
    private Platform platform;
}
