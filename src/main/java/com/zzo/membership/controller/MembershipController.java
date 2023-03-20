package com.zzo.membership.controller;

import com.zzo.membership.dao.MembershipDao;
import com.zzo.membership.dto.MembershipDto;
import com.zzo.membership.service.MembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static com.zzo.membership.constr.MembershipConstant.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipDao> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Valid final MembershipDto membershipDto){

        final MembershipDao membershipDao = membershipService.addMembership(userId, membershipDto.getMembershipType(), membershipDto.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(membershipDao);
    }

}


