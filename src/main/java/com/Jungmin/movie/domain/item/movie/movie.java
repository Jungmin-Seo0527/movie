package com.Jungmin.movie.domain.item.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static lombok.AccessLevel.*;

@Entity
@Getter @Setter(PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class movie {

    @Id @GeneratedValue
    private Long id;

    @Column
    private String title;

    @Column
    private int rank;

    @Column
    private int price;

    @Column
    private String genre;

    @Column
    private String url;

}
