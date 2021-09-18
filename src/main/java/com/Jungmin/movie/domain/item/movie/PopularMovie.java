package com.Jungmin.movie.domain.item.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter @Setter(PRIVATE) @Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularMovie {

    @Id @GeneratedValue
    @Column(name = "popularMovie_id")
    private Long id;

    private int rank;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
