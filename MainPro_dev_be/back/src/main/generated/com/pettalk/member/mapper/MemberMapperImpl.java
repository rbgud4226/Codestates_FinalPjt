package com.pettalk.member.mapper;

import com.pettalk.member.dto.PatchMemberDto;
import com.pettalk.member.dto.PostMemberDto;
import com.pettalk.member.entity.Member;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-25T16:41:33+0900",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 11.0.14.1 (Azul Systems, Inc.)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public Member memberPostToMember(PostMemberDto requestBody) {
        if ( requestBody == null ) {
            return null;
        }

        Member member = new Member();

        member.setNickName( requestBody.getNickName() );
        member.setEmail( requestBody.getEmail() );
        member.setProfileImage( requestBody.getProfileImage() );
        member.setPhone( requestBody.getPhone() );
        member.setPassword( requestBody.getPassword() );

        return member;
    }

    @Override
    public Member memberPatchToMember(PatchMemberDto requestBody) {
        if ( requestBody == null ) {
            return null;
        }

        Member member = new Member();

        member.setNickName( requestBody.getNickName() );

        return member;
    }
}
