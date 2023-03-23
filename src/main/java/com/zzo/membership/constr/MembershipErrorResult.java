package com.zzo.membership.constr;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MembershipErrorResult {

    //httpStatus와 message를 세팅 해줌으로써, 디버깅시에 더빠르게 체크가능
    DUPLICATED_MEMBERSHIP_REGISTER(HttpStatus.BAD_REQUEST, "Duplicated Membership Register Request"),
    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Exception"),
    MEMBERSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "Membership Not found"),
    MEMBERSHIP_NOT_OWNER(HttpStatus.BAD_REQUEST, "Membership Not owner"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
