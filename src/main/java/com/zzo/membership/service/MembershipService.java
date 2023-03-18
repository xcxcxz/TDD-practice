package com.zzo.membership.service;

import com.zzo.membership.constr.MembershipErrorResult;
import com.zzo.membership.constr.MembershipException;
import com.zzo.membership.constr.MembershipType;
import com.zzo.membership.dao.MembershipDao;
import com.zzo.membership.entity.Membership;
import com.zzo.membership.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private MembershipRepository membershipRepository;

    public MembershipDao addMembership(final String userId, final MembershipType membershipType, final Integer point) {
        final Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
        if(result != null){
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }

        final Membership membership = Membership.builder()
                .userId(userId)
                .membershipType(membershipType)
                .point(point)
                .build();
        final Membership savedMembership = membershipRepository.save(membership);
        return MembershipDao.builder()
                .id(savedMembership.getId())
                .membershipType(savedMembership.getMembershipType())
                .build();
    }

}
