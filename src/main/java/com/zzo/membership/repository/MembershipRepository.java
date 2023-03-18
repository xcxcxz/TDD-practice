package com.zzo.membership.repository;

import com.zzo.membership.constr.MembershipType;
import com.zzo.membership.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Membership findByUserIdAndMembershipType(final String userId, final MembershipType membershipType);
}
