package com.zzo.membership.dao;

import com.zzo.membership.constr.MembershipType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MembershipDao {

    private final Long id;
    private final MembershipType membershipType;

}
