package com.zpi.tripgroupservice.trip_group;

import com.zpi.tripgroupservice.commons.Currency;
import com.zpi.tripgroupservice.commons.GroupStage;
import com.zpi.tripgroupservice.dto.*;
import com.zpi.tripgroupservice.exception.ApiPermissionException;
import com.zpi.tripgroupservice.exception.ApiRequestException;
import com.zpi.tripgroupservice.mapper.MapStructMapper;
import com.zpi.tripgroupservice.proxy.FinanceProxy;
import com.zpi.tripgroupservice.security.CustomUsernamePasswordAuthenticationToken;
import com.zpi.tripgroupservice.user_group.UserGroupService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static com.zpi.tripgroupservice.commons.GroupStage.AFTER_TRIP_STAGE;
import static com.zpi.tripgroupservice.commons.GroupStage.TRIP_STAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class TripGroupServiceTest {
    @MockBean
    TripGroupRepository tripGroupRepository;

    @MockBean
    UserGroupService userGroupService;

    @Autowired
    MapStructMapper mapStructMapper;

    @MockBean
    FinanceProxy financeProxy;

    @Captor
    ArgumentCaptor<TripGroup> tripGroupArgumentCaptor;

    @Autowired
    @InjectMocks
    TripGroupService tripGroupService;

    void mockAuthorizePartOfTheGroupAspect(){
        Authentication authentication = new CustomUsernamePasswordAuthenticationToken(null, null, 1L);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        doReturn(Boolean.TRUE).when(userGroupService).checkIfUserIsInGroup(any(), any());
    }

    void mockAuthorizeCoordinatorAspect(){
        Authentication authentication = new CustomUsernamePasswordAuthenticationToken(null, null, 1L);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        doReturn(Boolean.TRUE).when(userGroupService).isUserCoordinator(any(), any());
    }

    void mockCustomUsernamePasswordAuthentication(){
        Authentication authentication = new CustomUsernamePasswordAuthenticationToken(null, null, 1L);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void shouldGetAllGroupsForUser() {
        //given
        var groups = List.of(new TripGroup("Test", Currency.PLN, "Desc", "Wroclaw"));
        groups.get(0).setGroupId(0L);

        //when
        when(tripGroupRepository.findAllGroupsForUser(anyLong())).thenReturn(groups);
        when(userGroupService.getNumberOfParticipants(any())).thenReturn(2);
        var actualResult = tripGroupService.getAllGroupsForUser(1L);

        var expectedGroups = List.of(new TripExtendedDataDto(0L,"Test", Currency.PLN, "Desc", "Wrocław"
        , null, null, null, null, null ,null));
        //then
        verify(tripGroupRepository, times(1)).findAllGroupsForUser(anyLong());
        verify(userGroupService, times(1)).getNumberOfParticipants(any());
        assertThat(actualResult).hasSameElementsAs(expectedGroups);
    }

    @Test
    void shouldThrowErrorWhenUserIdInvalidGetAllGroupsById() {
        //when
        var exception = assertThrows(IllegalArgumentException.class,
                () -> tripGroupService.getAllGroupsForUser(null));

        //then
        verify(tripGroupRepository, never()).findAllGroupsForUser(anyLong());
        assertThat(exception.getMessage()).isEqualTo("User id is invalid. Id must be a positive number");
    }

    @Test
    void shouldReturnTripGroupById() {
        //given
        mockAuthorizePartOfTheGroupAspect();
        var group = new TripGroup("Name", Currency.PLN, "Desc", "Wroclaw");

        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.of(group));
        var result = tripGroupService.getTripGroupById(1L);

        //then
        verify(tripGroupRepository, times(1)).findById(anyLong());
        assertThat(result).isEqualTo(group);
    }

    @Test
    void shouldThrowErrorWhenGroupIdInvalidGetGroupById() {
        //given
        mockAuthorizePartOfTheGroupAspect();

        //when
        var exception = assertThrows(IllegalArgumentException.class,
                () -> tripGroupService.getTripGroupById(-2L));

        //then
        verify(tripGroupRepository, never()).findById(anyLong());
        assertThat(exception.getMessage()).isEqualTo("Group id is invalid. Id must be a positive number");
    }

    @Test
    void shouldThrowErrorWhenTripGroupIsNotFound() {
        //given
        mockAuthorizePartOfTheGroupAspect();

        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.empty());
        var exception = assertThrows(ApiRequestException.class,
                                     () -> tripGroupService.getTripGroupById(1L));

        //then
        verify(tripGroupRepository, times(1)).findById(anyLong());
        assertThat(exception.getMessage()).isEqualTo("There are no group for given id: " + 1L);
    }

//    @Test
//    void shouldSuccessfullyCreateGroup() {
//        mockCustomUsernamePasswordAuthentication();
//        //given
//        var tripGroupDto = new TripGroupDto("Name", Currency.PLN, "Desc", "Wroclaw");
//        Double[] coordinates = { 11.22, 22.33 };
//
//        //when
//        when(geolocation.findCoordinates(anyString())).thenReturn(coordinates);
//        var actualTripGroup = tripGroupService.createGroup(tripGroupDto);
//
//        //then
//        var expectedTripGroup = new TripGroup("Name", Currency.PLN, "Desc", "Raclawicka");
//        expectedTripGroup.setLatitude(11.22);
//        expectedTripGroup.setLongitude(22.33);
//        assertThat(actualTripGroup).isEqualTo(expectedTripGroup);
//        verify(geolocation, times(1)).findCoordinates(anyString());
//        verify(tripGroupRepository, times(1)).save(any());
//        verify(userGroupService, times(1)).createUserGroup(anyLong(), any());
//    }

    @Test
    void shouldSuccessfullyDeleteGroup() {
        //given
        mockAuthorizeCoordinatorAspect();

        //when
        tripGroupService.deleteGroup(1L);

        //then
        verify(tripGroupRepository, times(1)).deleteById(1L);
        verify(userGroupService, times(1)).deletionGroupCleanUp(1L);
    }

    @Test
    void shouldThrowErrorWhenUserIdOrGroupIdIsInvalidInDeleteGroup() {
        //given
        mockAuthorizeCoordinatorAspect();

        //when
        var exception = assertThrows(IllegalArgumentException.class,
                () -> tripGroupService.deleteGroup(null));

        //then
        verify(tripGroupRepository, never()).deleteById(anyLong());
        verify(userGroupService, never()).deletionGroupCleanUp(anyLong());
        assertThat(exception.getMessage()).isEqualTo("Group id is invalid. Id must be a positive number");
    }

    @Test
    void shouldCorrectlyUpdateTripGroup() {
        //given
        mockAuthorizeCoordinatorAspect();
        var tripGroupDto = new TripGroupDto("Name", Currency.EUR, "Updated Desc", "China");
        var tripGroup = new TripGroup("Name", Currency.PLN, "Desc", "Wroclaw");

        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.of(tripGroup));
        var actualTripGroup = tripGroupService.updateGroup(1L, tripGroupDto);

        //then
        var expectedTripGroup = new TripGroup("Name", Currency.PLN, "Desc", "Wroclaw");
        assertThat(actualTripGroup).isEqualTo(expectedTripGroup);
        verify(tripGroupRepository, times(1)).save(any());
        verify(tripGroupRepository, times(1)).findById(anyLong());
    }

    @Test
    void shouldThrowErrorWhenTripGroupNotFoundUpdateGroup() {
        //given
        mockAuthorizeCoordinatorAspect();

        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.empty());
        var exception = assertThrows(ApiRequestException.class,
                () -> tripGroupService.updateGroup(1L, null));

        //then
        assertThat(exception.getMessage()).isEqualTo("There is no group with given group_id ");
        verify(tripGroupRepository, never()).save(any());
        verify(tripGroupRepository, times(1)).findById(anyLong());
    }

    @Test
    void shouldGetTripData() {
        //given
        var tripGroup = new TripGroup("Name", Currency.PLN, "Desc", "Wroclaw");
        tripGroup.setGroupId(1L);
        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.of(tripGroup));
        when(userGroupService.getNumberOfParticipants(anyLong())).thenReturn(2);
        var actualResult = tripGroupService.getTripData(1L);

        //then
        var expectedResult = new TripExtendedDataDto(tripGroup.getGroupId(), tripGroup.getName(), tripGroup.getCurrency(), tripGroup.getDescription(),
                tripGroup.getDestinationLocation(), tripGroup.getStartDate(),
                tripGroup.getEndDate(), tripGroup.getLatitude(), tripGroup.getLongitude(), tripGroup.getGroupStage(), 2);
        assertThat(actualResult).isEqualTo(expectedResult);
        verify(tripGroupRepository, times(1)).findById(anyLong());
        verify(userGroupService, times(1)).getNumberOfParticipants(anyLong());
    }

    @Test
    void shouldThrowExceptionWhenGroupNotFound() {
        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.empty());
        var exception = assertThrows(ApiRequestException.class,
                () -> tripGroupService.getTripData(1L));

        //then
        assertThat(exception.getMessage()).isEqualTo("There is no group with given group_id ");
        verify(tripGroupRepository, times(1)).findById(anyLong());
    }


    @Test
    void shouldCorrectlySetCurrencyInGroup() {
        //given
        mockAuthorizeCoordinatorAspect();
        var currency = Currency.EUR;
        var tripGroup = new TripGroup("Name", Currency.PLN, "Desc", "Wroclaw");

        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.of(tripGroup));
        when(tripGroupRepository.save(any(TripGroup.class))).thenAnswer(i -> i.getArguments()[0]);
        var actualResult = tripGroupService.setCurrencyInGroup(1L, currency);

        //then
        assertThat(actualResult.getCurrency()).isEqualTo(currency);
        verify(tripGroupRepository, times(1)).findById(anyLong());
        verify(tripGroupRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowErrorWhenTripGroupNotFoundSetCurrency() {
        //given
        mockAuthorizeCoordinatorAspect();

        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.empty());
        var exception = assertThrows(ApiRequestException.class,
                () -> tripGroupService.setCurrencyInGroup(1L, Currency.PLN));

        //then
        assertThat(exception.getMessage()).isEqualTo("There is no group with given group_id ");
        verify(tripGroupRepository, times(1)).findById(anyLong());
        verify(tripGroupRepository, never()).save(any());
    }

    @Test
    void shouldBeAbleToLeaveGroupTripStage() {
        //given
        mockAuthorizePartOfTheGroupAspect();
        var tripGroup = new TripGroup("Name", Currency.PLN, "Desc", "Wroclaw");
        tripGroup.setGroupStage(TRIP_STAGE);
        //when
        when(financeProxy.isDebtorOrDebteeToAnyFinancialRequests(anyString(), anyLong(), anyLong())).thenReturn(false);
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.of(tripGroup));
        when(userGroupService.getAllCoordinatorsIdsInGroup(anyLong())).thenReturn(List.of(1L, 2L));
        when(userGroupService.isUserCoordinator(anyLong(), anyLong())).thenReturn(false);
        tripGroupService.leaveGroup( 1L);

        //then
        verify(financeProxy, times(1)).isDebtorOrDebteeToAnyFinancialRequests(anyString(), anyLong(), anyLong());
        verify(userGroupService, times(1)).deleteUserFromGroup(anyLong(), anyLong());
    }

    @Test
    void shouldBeAbleToLeaveGroupPlanningStage() {
        //given
        mockAuthorizePartOfTheGroupAspect();
        var tripGroup = new TripGroup("Name", Currency.PLN, "Desc", "Wroclaw");
        tripGroup.setGroupStage(GroupStage.PLANNING_STAGE);
        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.of(tripGroup));
        when(userGroupService.getAllCoordinatorsIdsInGroup(anyLong())).thenReturn(List.of(1L, 2L));
        when(userGroupService.isUserCoordinator(anyLong(), anyLong())).thenReturn(false);
        tripGroupService.leaveGroup( 1L);

        //then
        verify(userGroupService, times(1)).deleteUserFromGroup(anyLong(), anyLong());
    }

    @Test
    void shouldThrowErrorWhenUserHasUnsettledFinancialRequestsTripStage() {
        //given
        mockAuthorizePartOfTheGroupAspect();
        var tripGroup = new TripGroup("Name", Currency.PLN, "Desc", "Wroclaw");
        tripGroup.setGroupStage(TRIP_STAGE);
        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.of(tripGroup));
        when(financeProxy.isDebtorOrDebteeToAnyFinancialRequests(anyString(), anyLong(), anyLong())).thenReturn(true);

        var exception = assertThrows(ApiPermissionException.class,
                () -> tripGroupService.leaveGroup( 1L));

        //then
        assertThat(exception.getMessage()).isEqualTo("You cannot leave group if you have unsettled expenses");
        verify(financeProxy, times(1)).isDebtorOrDebteeToAnyFinancialRequests(anyString(), anyLong(), anyLong());
        verify(userGroupService, never()).deleteUserFromGroup(anyLong(), anyLong());
    }

    @Test
    void shouldThrowErrorWhenTripGroupNotFoundLeaveGroup() {
        //given
        mockAuthorizePartOfTheGroupAspect();

        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.empty());

        var exception = assertThrows(ApiRequestException.class,
                () -> tripGroupService.leaveGroup( 1L));

        //then
        assertThat(exception.getMessage()).isEqualTo("There is no group with given group_id ");
        verify(tripGroupRepository, times(1)).findById(anyLong());
        verify(userGroupService, never()).deleteUserFromGroup(anyLong(), anyLong());
    }

    @Test
    void shouldThrowErrorWhenUserIsLastCoordinatorTripStage() {
        //given
        mockAuthorizePartOfTheGroupAspect();
        var tripGroup = new TripGroup("Name", Currency.PLN, "Desc", "Wroclaw");
        tripGroup.setGroupStage(TRIP_STAGE);
        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.of(tripGroup));
        when(financeProxy.isDebtorOrDebteeToAnyFinancialRequests(anyString(), anyLong(), anyLong())).thenReturn(false);
        when(userGroupService.getAllCoordinatorsIdsInGroup(anyLong())).thenReturn(List.of(1L));
        when(userGroupService.isUserCoordinator(anyLong(), anyLong())).thenReturn(true);
        var exception = assertThrows(ApiPermissionException.class,
                () -> tripGroupService.leaveGroup( 1L));

        //then
        assertThat(exception.getMessage()).isEqualTo("You cannot leave the group because you are last coordinator in group");
        verify(financeProxy, times(1)).isDebtorOrDebteeToAnyFinancialRequests(anyString(), anyLong(), anyLong());
        verify(userGroupService, never()).deleteUserFromGroup(anyLong(), anyLong());
    }

    @Test
    void shouldThrowErrorWhenUserIsLastCoordinatorPlanningStage() {
        //given
        mockAuthorizePartOfTheGroupAspect();
        var tripGroup = new TripGroup("Name", Currency.PLN, "Desc", "Wroclaw");
        tripGroup.setGroupStage(GroupStage.PLANNING_STAGE);
        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.of(tripGroup));
        when(userGroupService.getAllCoordinatorsIdsInGroup(anyLong())).thenReturn(List.of(1L));
        when(userGroupService.isUserCoordinator(anyLong(), anyLong())).thenReturn(true);
        var exception = assertThrows(ApiPermissionException.class,
                () -> tripGroupService.leaveGroup( 1L));

        //then
        assertThat(exception.getMessage()).isEqualTo("You cannot leave the group because you are last coordinator in group");
        verify(userGroupService, never()).deleteUserFromGroup(anyLong(), anyLong());
    }


    @Test
    void shouldBeAbleToChangeGroupStagePlanningStage() {
        //given
        var tripGroup = new TripGroup("Name", Currency.PLN, "Desc", "Wroclaw");
        tripGroup.setGroupStage(GroupStage.PLANNING_STAGE);

        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.of(tripGroup));
        tripGroupService.changeGroupStage(1L);

        //then
        verify(tripGroupRepository, times(1)).save(tripGroupArgumentCaptor.capture());
        var resultTripGroup = tripGroupArgumentCaptor.getValue();
        assertEquals(TRIP_STAGE, resultTripGroup.getGroupStage());
    }

    @Test
    void shouldBeAbleToChangeGroupStageTripStage() {
        //given
        var tripGroup = new TripGroup("Name", Currency.PLN, "Desc", "Wroclaw");
        tripGroup.setGroupStage(TRIP_STAGE);

        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.of(tripGroup));
        tripGroupService.changeGroupStage(1L);

        //then
        verify(tripGroupRepository, times(1)).save(tripGroupArgumentCaptor.capture());
        var resultTripGroup = tripGroupArgumentCaptor.getValue();
        assertEquals(AFTER_TRIP_STAGE, resultTripGroup.getGroupStage());
    }

    @Test
    void shouldReturnExceptionWhenTripGroupNotFoundInChangeGroupStage() {
        //given
        Long groupId = 1L;

        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.empty());
        var exception = assertThrows(ApiRequestException.class,
                () -> tripGroupService.changeGroupStage(1L));

        //then
        verify(tripGroupRepository, times(1)).findById(anyLong());
        assertThat(exception.getMessage()).isEqualTo("There is no group with given group_id ");
    }


    @Test
    void shouldReturnExceptionWhenTripGroupNotFoundInDeleteUserFromGroup() {
        //given
        mockAuthorizeCoordinatorAspect();
        //when
        when(tripGroupRepository.findById(anyLong())).thenReturn(Optional.empty());
        var exception = assertThrows(ApiRequestException.class,
                () -> tripGroupService.deleteUserFromGroup(1L, 1L));

        //then
        verify(tripGroupRepository, times(1)).findById(anyLong());
        assertThat(exception.getMessage()).isEqualTo("There is no group with given group_id ");
    }
}