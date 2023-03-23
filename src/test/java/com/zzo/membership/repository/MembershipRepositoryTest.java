package com.zzo.membership.repository;

import com.zzo.membership.constr.MembershipType;
import com.zzo.membership.entity.Membership;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Member;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class MembershipRepositoryTest {

    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    void membershipRegist(){
        //멤버십 데이터 저장
        //given
        final Membership membership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();


        //when
        final Membership result = membershipRepository.save(membership);


        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("userId");
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(result.getPoint()).isEqualTo(10000);
    }

    @Test
    void membershipExist(){
        //멤버십 존재여부 확인
        //given
        final Membership membership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        //when
        membershipRepository.save(membership);
        final Membership findResult = membershipRepository.findByUserIdAndMembershipType("userId", MembershipType.NAVER);


        //then
        assertThat(findResult.getId()).isNotNull();
        assertThat(findResult.getUserId()).isEqualTo("userId");
        assertThat(findResult.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(findResult.getPoint()).isEqualTo(10000);
    }

    @Test
    void membershipSizeZero(){
        //given


        //when
        List<Membership> result = membershipRepository.findAllByUserId("userId");

        //then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void membershipSizeTwo(){
        //given
        final Membership naverMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        final Membership kakaoMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.KAKAO)
                .point(10000)
                .build();

        membershipRepository.save(naverMembership);
        membershipRepository.save(kakaoMembership);
        //when

        List<Membership> result = membershipRepository.findAllByUserId("userId");

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void deleteMembership(){
        //given
        final Membership naverMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        final Membership savedMembership = membershipRepository.save(naverMembership);
        //when
    membershipRepository.deleteById(savedMembership.getId());

        //then
    }

}