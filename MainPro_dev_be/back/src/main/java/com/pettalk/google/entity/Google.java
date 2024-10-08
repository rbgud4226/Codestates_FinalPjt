package com.pettalk.google.entity;

import com.pettalk.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Google {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column
    private String refreshToken;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Member member;
}
