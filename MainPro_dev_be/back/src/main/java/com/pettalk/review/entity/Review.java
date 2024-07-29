package com.pettalk.review.entity;


import com.pettalk.petsitter.entity.PetSitter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    @Column
    private String content;
    @Column
    private String memberNickName;
    @Column
    private Float star;
    @Column
    private Long wcBoardId;
//    @Column
//    private String petSitterImage;
//    @Column
//    private String petSitterEmail;

    @ManyToOne
    @JoinColumn(name = "petSitter_id")
    private PetSitter petSitter;
}
