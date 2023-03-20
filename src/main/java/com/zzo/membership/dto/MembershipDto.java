package com.zzo.membership.dto;

import com.zzo.membership.constr.MembershipType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MembershipDto {
        @NotNull
        @Min(0)
        private final Integer point;
        @NotNull
        private final MembershipType membershipType;
}
