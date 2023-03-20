package com.zzo.membership.controller;

import com.google.gson.Gson;
import com.zzo.membership.constr.MembershipErrorResult;
import com.zzo.membership.constr.MembershipException;
import com.zzo.membership.constr.MembershipType;
import com.zzo.membership.dao.MembershipDao;
import com.zzo.membership.dto.MembershipDto;
import com.zzo.membership.global.GlobalExceptionHandler;
import com.zzo.membership.service.MembershipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static com.zzo.membership.constr.MembershipConstant.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

   @InjectMocks
   private MembershipController target;

   @Mock
   private MembershipService membershipService;

   //WebMvcTest 또한 사용할 수 있으나, 속도가 느림.
   private MockMvc mockMvc;
   private Gson gson;

   @BeforeEach
   //test별 객체 초기화를 위함
   public void init() {
       gson = new Gson();
       mockMvc = MockMvcBuilders.standaloneSetup(target)
               .setControllerAdvice(new GlobalExceptionHandler())
               .build();
   }

    @Test
    void userIdNull() throws Exception {
        //given
        final String url = "/api/v1/memberships";

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then

        resultActions.andExpect(status().isBadRequest());
    }

    private MembershipDto membershipRequest(final Integer point, final MembershipType membershipType){
       return MembershipDto.builder()
               .point(point)
               .membershipType(membershipType)
               .build();
    }

    // 포인트가 음수이거나 null인 경우 또는 멤버십 타입이 null인 경우
    
    @Test
    void isPointMinus() throws Exception {
        //given
        final String url = "/api/v1/memberships";
    
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(-1, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    
        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void isPointNull() throws Exception {
        //given
        final String url = "/api/v1/memberships";


        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(null, MembershipType.NAVER)))
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void isMembershipTypeNull() throws Exception {
        //given
        String url = "/api/v1/memberships";

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(1000, null)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    // 위 3개의 테스트는 동일한 검증하고, 파라미터만 다른 테스트이다.
    // 아래와 같이 리팩토링 할 수있다.
    @ParameterizedTest
    @MethodSource("invalidMembershipAddParameter")
    void wrongParamTest(final Integer point, final MembershipType membershipType) throws Exception {
        //given
        final String url = "/api/v1/memberships";

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(point, membershipType)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidMembershipAddParameter(){
       return Stream.of(
               Arguments.of(null, MembershipType.NAVER),
               Arguments.of(-1, MembershipType.NAVER),
               Arguments.of(10000, null)
       );
    }


    @Test
    void errorThrowFromService() throws Exception {
        //given
        final String url = "/api/v1/memberships";
        doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
                .when(membershipService)
                .addMembership("12345", MembershipType.NAVER, 10000);

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void membershipRegistSucc() throws Exception {
        //given
        final String url = "/api/v1/memberships";
        final MembershipDao membershipDao = MembershipDao.builder()
                .id(-1L)
                .membershipType(MembershipType.NAVER)
                .build();

        doReturn(membershipDao).when(membershipService).addMembership("12345", MembershipType.NAVER, 10000);
        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isCreated());

        final MembershipDao response = gson.fromJson(resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), MembershipDao.class);

        assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(response.getId()).isNotNull();
    }
}
