package org.folio.edge.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.folio.edge.users.TestUtil;
import org.folio.edge.users.client.FeesFinesClient;
import org.folio.edge.users.client.PatronBlocksClient;
import org.folio.edge.users.client.UsersClient;
import org.folio.users.domain.dto.AutomatedPatronBlockResponse;
import org.folio.users.domain.dto.ManualBlocksResponse;
import org.folio.users.domain.dto.RequestQueryParameters;
import org.folio.users.domain.dto.UserGroup;
import org.folio.users.domain.dto.UserResults;
import org.folio.users.domain.dto.Userdata;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.folio.edge.users.TestConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UsersServiceTest {

  private static final String USER_ID = "a528d25c-9150-4a7f-906f-d62758059747";
  @InjectMocks
  private UsersService usersService;
  @Mock
  private UsersClient usersClient;
  @Mock
  private FeesFinesClient feesFinesClient;
  @Mock
  private PatronBlocksClient patronBlocksClient;

  @BeforeEach
  void setUp() throws JsonProcessingException {
    var manualBlocksContent = TestUtil.readFileContentFromResources(TestConstant.MANUAL_BLOCKS_EMPTY_PATH);
    var manualBlocksResponse = TestUtil.OBJECT_MAPPER.readValue(manualBlocksContent, ManualBlocksResponse.class);
    when(feesFinesClient.getManualBlocks(any(RequestQueryParameters.class))).thenReturn(manualBlocksResponse);
    var automatedPatronBlocksContent = TestUtil.readFileContentFromResources(
        TestConstant.AUTOMATED_PATRON_BLOCKS_EMPTY_PATH);
    var automatedPatronBlockResponse = TestUtil.OBJECT_MAPPER.readValue(automatedPatronBlocksContent,
        AutomatedPatronBlockResponse.class);
    when(patronBlocksClient.getAutomatedPatronBlocks(anyString())).thenReturn(automatedPatronBlockResponse);
  }

  @Test
  void createUser_shouldCreateUserAndReturnCreatedUser() throws JsonProcessingException {
    var userdataContent = TestUtil.readFileContentFromResources(TestConstant.USERDATA_PATH);
    var expectedUserdata = TestUtil.OBJECT_MAPPER.readValue(userdataContent, Userdata.class);
    when(usersClient.createUser(TestConstant.LANG_PARAM_VALID_EN, expectedUserdata)).thenReturn(expectedUserdata);

    var actualUserdata = usersService.createUser(TestConstant.LANG_PARAM_VALID_EN, expectedUserdata);

    assertEquals(expectedUserdata, actualUserdata);
  }

  @Test
  void usersExistsByQuery_shouldReturnTrueFor2Users() throws JsonProcessingException {
    var userdataContent = TestUtil.readFileContentFromResources(TestConstant.BOTH_USER_COLLECTION_PATH);
    var userResults = TestUtil.OBJECT_MAPPER.readValue(userdataContent, UserResults.class);
    var requestQueryParameters = new RequestQueryParameters();
    requestQueryParameters.setQuery(String.format("username=%s or personal.email=%s", "test", "petra@herzog-group.mu"));
    when(usersClient.findUsers(requestQueryParameters)).thenReturn(userResults);

    var exists = usersService.usersExistsByQuery(requestQueryParameters);

    assertTrue(exists);
  }

  @Test
  void usersExistsByQuery_shouldReturnTrueFor1User() throws JsonProcessingException {
    var userdataContent = TestUtil.readFileContentFromResources(TestConstant.ONEOF_USER_COLLECTION_PATH);
    var userResults = TestUtil.OBJECT_MAPPER.readValue(userdataContent, UserResults.class);
    var requestQueryParameters = new RequestQueryParameters();
    requestQueryParameters.setQuery(String.format("username=%s or personal.email=%s", "test", "petra@herzog-group.mu"));
    when(usersClient.findUsers(requestQueryParameters)).thenReturn(userResults);

    var exists = usersService.usersExistsByQuery(requestQueryParameters);

    assertTrue(exists);
  }

  @Test
  void usersExistsByQuery_shouldReturnFalseForNoUsers() throws JsonProcessingException {
    var userdataContent = TestUtil.readFileContentFromResources(TestConstant.EMPTY_USER_COLLECTION_PATH);
    var userResults = TestUtil.OBJECT_MAPPER.readValue(userdataContent, UserResults.class);
    var requestQueryParameters = new RequestQueryParameters();
    requestQueryParameters.setQuery(String.format("username=%s or personal.email=%s", "test", "petra@herzog-group.mu"));
    when(usersClient.findUsers(requestQueryParameters)).thenReturn(userResults);

    boolean exists = usersService.usersExistsByQuery(requestQueryParameters);

    assertFalse(exists);
  }

  @Test
  void getUsers_shouldReturnAllUsers() throws JsonProcessingException {
    var userdataContent = TestUtil.readFileContentFromResources(TestConstant.BOTH_USER_COLLECTION_PATH);
    var expectedUserResults = TestUtil.OBJECT_MAPPER.readValue(userdataContent, UserResults.class);
    var requestQueryParameters = new RequestQueryParameters();
    requestQueryParameters.setQuery(String.format("username=%s or personal.email=%s", "test", "petra@herzog-group.mu"));
    when(usersClient.findUsers(requestQueryParameters)).thenReturn(expectedUserResults);
    var usergroupContent = TestUtil.readFileContentFromResources(TestConstant.USERGROUP_PATH);
    var expectedUserGroup = TestUtil.OBJECT_MAPPER.readValue(usergroupContent, UserGroup.class);
    when(usersClient.getGroupById(anyString())).thenReturn(expectedUserGroup);

    var actualUserResults = usersService.getUsers(requestQueryParameters);

    assertEquals(expectedUserResults, actualUserResults);
  }

  @Test
  void getUsers_shouldReturnUser1forBothUsersCollection() throws JsonProcessingException {
    var userdataContent = TestUtil.readFileContentFromResources(TestConstant.ONEOF_USER_COLLECTION_PATH);
    var expectedUserResults = TestUtil.OBJECT_MAPPER.readValue(userdataContent, UserResults.class);
    var requestQueryParameters = new RequestQueryParameters();
    requestQueryParameters.setQuery(String.format("username=%s or personal.email=%s", "test", "petra@herzog-group.mu"));
    when(usersClient.findUsers(requestQueryParameters)).thenReturn(expectedUserResults);
    var usergroupContent = TestUtil.readFileContentFromResources(TestConstant.USERGROUP_PATH);
    var expectedUserGroup = TestUtil.OBJECT_MAPPER.readValue(usergroupContent, UserGroup.class);
    when(usersClient.getGroupById(anyString())).thenReturn(expectedUserGroup);

    var actualUserResults = usersService.getUsers(requestQueryParameters);

    assertEquals(expectedUserResults, actualUserResults);
  }

  @Test
  void getUsers_shouldReturnNotBlockedUser() throws JsonProcessingException {
    var userdataContent = TestUtil.readFileContentFromResources(TestConstant.ONEOF_USER_COLLECTION_PATH);
    var expectedUserResults = TestUtil.OBJECT_MAPPER.readValue(userdataContent, UserResults.class);
    var requestQueryParameters = new RequestQueryParameters();
    requestQueryParameters.setQuery(String.format("username=%s or personal.email=%s", "test", "petra@herzog-group.mu"));
    when(usersClient.findUsers(requestQueryParameters)).thenReturn(expectedUserResults);
    var usergroupContent = TestUtil.readFileContentFromResources(TestConstant.USERGROUP_PATH);
    var expectedUserGroup = TestUtil.OBJECT_MAPPER.readValue(usergroupContent, UserGroup.class);
    when(usersClient.getGroupById(anyString())).thenReturn(expectedUserGroup);

    var actualUserResults = usersService.getUsers(requestQueryParameters);

    assertEquals(expectedUserResults, actualUserResults);
    assertFalse(actualUserResults.getUsers().get(0).getBlocked());
  }

  @Test
  void getUsers_shouldReturnBlockedUser_whenManualBlocksPresent() throws JsonProcessingException {
    var userdataContent = TestUtil.readFileContentFromResources(TestConstant.ONEOF_USER_COLLECTION_PATH);
    var expectedUserResults = TestUtil.OBJECT_MAPPER.readValue(userdataContent, UserResults.class);
    var userRequestParam = new RequestQueryParameters();
    userRequestParam.setQuery(String.format("username=%s or personal.email=%s", "test", "petra@herzog-group.mu"));
    when(usersClient.findUsers(userRequestParam)).thenReturn(expectedUserResults);
    var manualBlocksContent = TestUtil.readFileContentFromResources(TestConstant.MANUAL_BLOCKS_PATH);
    var manualBlocksResponse = TestUtil.OBJECT_MAPPER.readValue(manualBlocksContent, ManualBlocksResponse.class);
    var manualBlockRequestParam = new RequestQueryParameters();
    manualBlockRequestParam.setQuery(String.format("userId=%s", USER_ID));
    when(feesFinesClient.getManualBlocks(manualBlockRequestParam)).thenReturn(manualBlocksResponse);
    var automatedPatronBlocksContent = TestUtil.readFileContentFromResources(
        TestConstant.AUTOMATED_PATRON_BLOCKS_EMPTY_PATH);
    var automatedPatronBlockResponse = TestUtil.OBJECT_MAPPER.readValue(automatedPatronBlocksContent,
        AutomatedPatronBlockResponse.class);
    when(patronBlocksClient.getAutomatedPatronBlocks(USER_ID)).thenReturn(
        automatedPatronBlockResponse);
    var usergroupContent = TestUtil.readFileContentFromResources(TestConstant.USERGROUP_PATH);
    var expectedUserGroup = TestUtil.OBJECT_MAPPER.readValue(usergroupContent, UserGroup.class);
    when(usersClient.getGroupById(anyString())).thenReturn(expectedUserGroup);

    var actualUserResults = usersService.getUsers(userRequestParam);

    assertTrue(actualUserResults.getUsers().get(0).getBlocked());
  }

  @Test
  void getUsers_shouldReturnBlockedUser_whenAutomatedPatronBlocksPresent() throws JsonProcessingException {
    var userdataContent = TestUtil.readFileContentFromResources(TestConstant.ONEOF_USER_COLLECTION_PATH);
    var expectedUserResults = TestUtil.OBJECT_MAPPER.readValue(userdataContent, UserResults.class);
    var userRequestParam = new RequestQueryParameters();
    userRequestParam.setQuery(String.format("username=%s or personal.email=%s", "test", "petra@herzog-group.mu"));
    when(usersClient.findUsers(userRequestParam)).thenReturn(expectedUserResults);
    var manualBlocksContent = TestUtil.readFileContentFromResources(TestConstant.MANUAL_BLOCKS_EMPTY_PATH);
    var manualBlocksResponse = TestUtil.OBJECT_MAPPER.readValue(manualBlocksContent, ManualBlocksResponse.class);
    var manualBlockRequestParam = new RequestQueryParameters();
    manualBlockRequestParam.setQuery(String.format("userId=%s", USER_ID));
    when(feesFinesClient.getManualBlocks(manualBlockRequestParam)).thenReturn(manualBlocksResponse);
    var automatedPatronBlocksContent = TestUtil.readFileContentFromResources(TestConstant.AUTOMATED_PATRON_BLOCKS_PATH);
    var automatedPatronBlockResponse = TestUtil.OBJECT_MAPPER.readValue(automatedPatronBlocksContent,
        AutomatedPatronBlockResponse.class);
    when(patronBlocksClient.getAutomatedPatronBlocks(USER_ID)).thenReturn(
        automatedPatronBlockResponse);
    var usergroupContent = TestUtil.readFileContentFromResources(TestConstant.USERGROUP_PATH);
    var expectedUserGroup = TestUtil.OBJECT_MAPPER.readValue(usergroupContent, UserGroup.class);
    when(usersClient.getGroupById(anyString())).thenReturn(expectedUserGroup);

    var actualUserResults = usersService.getUsers(userRequestParam);

    assertTrue(actualUserResults.getUsers().get(0).getBlocked());
  }

  @Test
  void getUsers_shouldReturnEmpty() throws JsonProcessingException {
    var userdataContent = TestUtil.readFileContentFromResources(TestConstant.EMPTY_USER_COLLECTION_PATH);
    var expectedUserResults = TestUtil.OBJECT_MAPPER.readValue(userdataContent, UserResults.class);
    var requestQueryParameters = new RequestQueryParameters();
    requestQueryParameters.setQuery(String.format("username=%s or personal.email=%s", "test", "petra@herzog-group.mu"));
    when(usersClient.findUsers(requestQueryParameters)).thenReturn(expectedUserResults);

    var actualUserResults = usersService.getUsers(requestQueryParameters);

    assertEquals(expectedUserResults, actualUserResults);
  }


}
