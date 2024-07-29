package com.pettalk.petsitter.entity;

import com.pettalk.member.entity.Member;
import com.pettalk.review.entity.Review;
import com.pettalk.submit.entity.PetSitterApplicant;
import com.pettalk.wcboard.entity.WcBoard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PetSitter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petSitterId;
    private String name;
    private String petSitterImage;
    private String introduce;

    @Column
    private String nowJob;
    private boolean smoking;
    private String info;

    @ElementCollection
    private List<String> exAnimal;

    @JoinColumn(name = "member_id")
    @OneToOne
    private Member member;


    @OneToMany(mappedBy = "petSitter", cascade = CascadeType.ALL)
    private List<WcBoard> wcBoard;

    @OneToMany(mappedBy = "petSitter", cascade = CascadeType.ALL)
    private List<PetSitterApplicant> petSitterApplicant;

    private LocalDateTime createdAt = LocalDateTime.now();

    //    @OneToMany(mappedBy = "petSitter")
    //    private List<ChatRoom> room = new ArrayList<>();

}