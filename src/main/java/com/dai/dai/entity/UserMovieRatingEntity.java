package com.dai.dai.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@Table(name = "user_ratings")
public class UserMovieRatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "movie_id")
    private Long movie_id;

    @Column(name = "rating")
    private Integer rating;
}
