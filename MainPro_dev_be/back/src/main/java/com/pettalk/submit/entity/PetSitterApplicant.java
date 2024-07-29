package com.pettalk.submit.entity;

import com.pettalk.member.entity.Member;
import com.pettalk.petsitter.entity.PetSitter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class PetSitterApplicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petSitterApplicantId;

//    @ManyToOne
//    @JoinColumn(name = "wcboard_id")
//    private WcBoard wcboard;

    @ManyToOne
    @JoinColumn(name = "petSitter_id")
    private PetSitter petSitter;

    private Long wcboardId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

//    @OneToOne
//    @JoinColumn(name = )
//    private String startTime;
//    private String endTime;
}
