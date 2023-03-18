package com.zzo.membership;

import com.zzo.membership.repository.MembershipRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MembershipApplicationTests {

    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    void isitNull() {
    }

}
