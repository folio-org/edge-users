package org.folio.edge.users.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.folio.edge.users.BaseIntegrationTests;
import org.folio.edge.users.TestUtil;
import org.folio.edge.users.service.UsersService;
import org.folio.edge.users.TestConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class UsersControllerIT extends BaseIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UsersService userServices;

  @Test
  void createUser_shouldReturnNewUser_whenUserIsCreated() throws Exception {
    String userdataContent = TestUtil.readFileContentFromResources(TestConstant.USERDATA_PATH);
    doPost(mockMvc, TestConstant.USERS_URI, TestConstant.LANG_PARAM_NAME, TestConstant.LANG_PARAM_VALID_EN, userdataContent)
        .andExpect(status().isOk())
        .andExpect(jsonPath("username", is("mockuser9")))
        .andExpect(jsonPath("id", is("99999999-9999-4999-9999-999999999999")))
        .andExpect(jsonPath("active", is(true)))
        .andExpect(jsonPath("type", is("patron")));
  }

  @Test
  void createUser_shouldReturn400Error_whenLangParameterIsInvalid() throws Exception {
    String userdataContent = TestUtil.readFileContentFromResources(TestConstant.USERDATA_PATH);
    doPost(mockMvc, TestConstant.USERS_URI, TestConstant.LANG_PARAM_NAME, TestConstant.LANG_PARAM_INVALID_VALUE, userdataContent)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("errorMessage", is("createUser.lang: must match \"[a-zA-Z]{2}\"")));
  }

  @Test
  void createUser_shouldReturn401Error_whenApiKeyIsInvalid() throws Exception {
    String userdataContent = TestUtil.readFileContentFromResources(TestConstant.USERDATA_PATH);
    doPost(mockMvc, TestConstant.USERS_URI, TestConstant.LANG_PARAM_NAME, TestConstant.LANG_PARAM_INVALID_VALUE, userdataContent, TestConstant.INVALID_API_KEY)
        .andExpect(status().isUnauthorized());
  }

  @Test
  void createUser_shouldReturn500Error_whenRequestBodyIsEmpty() throws Exception {
    String emptyUserdataContent = TestUtil.readFileContentFromResources(TestConstant.EMPTY_USERDATA_PATH);
    doPost(mockMvc, TestConstant.USERS_URI, TestConstant.LANG_PARAM_NAME, TestConstant.LANG_PARAM_VALID_DE, emptyUserdataContent)
        .andExpect(status().isInternalServerError());
  }

  @Test
  void userExists_shouldReturn204_when2UsersFound() throws Exception {
    String query = String.format("username=%s or personal.email=%s", "test", "test@mail.com");
    mockMvc.perform(MockMvcRequestBuilders.get(TestConstant.USER_EXISTS_URI)
            .param("query", query)
            .headers(defaultHeadersWithAuthorization()))
        .andExpect(status().isNoContent());
  }

  @Test
  void userExists_shouldReturn204_when1UserFound() throws Exception {
    String query = String.format("username=%s or personal.email=%s", "nouser", "test@mail.com");
    mockMvc.perform(MockMvcRequestBuilders.get(TestConstant.USER_EXISTS_URI)
            .param("query", query)
            .headers(defaultHeadersWithAuthorization()))
        .andExpect(status().isNoContent());
  }

  @Test
  void userExists_shouldReturn404_whenNoUserFound() throws Exception {
    String query = String.format("username=%s or personal.email=%s", "nouser", "none@mail.com");
    mockMvc.perform(MockMvcRequestBuilders.get(TestConstant.USER_EXISTS_URI)
            .param("query", query)
            .headers(defaultHeadersWithAuthorization()))
        .andExpect(status().isNotFound());
  }

  @Test
  void getUsers_shouldReturn200_withAllUsers() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(TestConstant.USERS_URI)
            .headers(defaultHeadersWithAuthorization()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("users[0].username", is("test-user")))
        .andExpect(jsonPath("users[0].id", is("a528d25c-9150-4a7f-906f-d62758059747")))
        .andExpect(jsonPath("users[1].username", is("guiseppe")))
        .andExpect(jsonPath("users[1].id", is("66fe5bd9-1129-4b40-b54d-05b4c358463c")))
        .andExpect(jsonPath("users[1].blocked", is(false)))
        .andExpect(jsonPath("totalRecords", is(2)));
  }

  @Test
  void getUsers_shouldReturn200_withUserByQuery() throws Exception {
    String query = String.format("username=%s or personal.email=%s", "nouser", "test@mail.com");
    mockMvc.perform(MockMvcRequestBuilders.get(TestConstant.USERS_URI)
            .param("query", query)
            .headers(defaultHeadersWithAuthorization()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("users[0].username", is("test-user")))
        .andExpect(jsonPath("users[0].id", is("a528d25c-9150-4a7f-906f-d62758059747")))
        .andExpect(jsonPath("users[0].active", is(true)))
        .andExpect(jsonPath("totalRecords", is(1)));
  }

  @Test
  void getUsers_shouldReturnBlockedUser_whenManualBlockPresent() throws Exception {
    String query = String.format("username=%s or personal.email=%s", "nouser", "test@mail.com");
    mockMvc.perform(MockMvcRequestBuilders.get(TestConstant.USERS_URI)
            .param("query", query)
            .headers(defaultHeadersWithAuthorization()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("users[0].blocked", is(true)))
        .andExpect(jsonPath("totalRecords", is(1)));
  }

  @Test
  void getUsers_shouldReturnBlockedUser_whenAutomatedBlockPresent() throws Exception {
    String query = String.format("username=%s or personal.email=%s", "seconduser", "test2@mail.com");
    mockMvc.perform(MockMvcRequestBuilders.get(TestConstant.USERS_URI)
            .param("query", query)
            .headers(defaultHeadersWithAuthorization()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("users[0].blocked", is(true)))
        .andExpect(jsonPath("totalRecords", is(1)));
  }

  @Test
  void getUsers_shouldReturn200withZeroTotalRecords_whenNoUsersFound() throws Exception {
    String query = String.format("username=%s or personal.email=%s", "nouser", "none@mail.com");
    mockMvc.perform(MockMvcRequestBuilders.get(TestConstant.USERS_URI)
            .param("query", query)
            .headers(defaultHeadersWithAuthorization()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("totalRecords", is(0)));
  }

  @Test
  void setPin_shouldReturn201() throws Exception {
    String patronPinContent = TestUtil.readFileContentFromResources(TestConstant.PATRON_PIN_BODY_PATH);
    mockMvc.perform(MockMvcRequestBuilders.post(TestConstant.PATRON_PIN_URI)
            .content(patronPinContent)
            .headers(defaultHeadersWithAuthorization()))
        .andExpect(status().isCreated());
  }

  @Test
  void verifyPin_shouldReturn200() throws Exception {
    String patronPinContent = TestUtil.readFileContentFromResources(TestConstant.PATRON_PIN_BODY_PATH);
    mockMvc.perform(MockMvcRequestBuilders.post(TestConstant.PATRON_PIN_VERIFY_URI)
            .content(patronPinContent)
            .headers(defaultHeadersWithAuthorization()))
        .andExpect(status().isOk());
  }

  @Test
  void verifyPin_shouldReturn422_whenPinIsInvalid() throws Exception {
    String patronPinContent = TestUtil.readFileContentFromResources(TestConstant.INVALID_PIN_BODY_PATH);
    mockMvc.perform(MockMvcRequestBuilders.post(TestConstant.PATRON_PIN_VERIFY_URI)
            .content(patronPinContent)
            .headers(defaultHeadersWithAuthorization()))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("code", is(422)))
        .andExpect(jsonPath("errorMessage", is("PIN is invalid")));
  }
}
