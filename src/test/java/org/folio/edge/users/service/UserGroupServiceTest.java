package org.folio.edge.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.folio.edge.users.client.UserClient;
import org.folio.users.domain.dto.UserGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserGroupServiceTest {

  private static final String GROUP_ID = "3684a786-6671-4268-8ed0-9db82ebca60b";

  @InjectMocks
  private UserGroupService userGroupService;
  @Mock
  private UserClient userClient;

  @Test
  void getUserGroupById_shouldReturnUserGroup() {
    var expectedUserGroup = new UserGroup().id(GROUP_ID).group("undergraduate");
    when(userClient.getGroupById(GROUP_ID)).thenReturn(expectedUserGroup);

    var actualUserGroup = userGroupService.getUserGroupById(GROUP_ID);

    assertEquals(expectedUserGroup, actualUserGroup);
    verify(userClient).getGroupById(GROUP_ID);
  }
}
