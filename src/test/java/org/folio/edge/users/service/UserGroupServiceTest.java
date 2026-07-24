package org.folio.edge.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.folio.edge.users.client.UserClient;
import org.folio.spring.FolioExecutionContext;
import org.folio.users.domain.dto.UserGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(classes = {UserGroupService.class, UserGroupServiceTest.TestContextConfiguration.class},
    webEnvironment = NONE)
class UserGroupServiceTest {

  private static final String USERGROUP_CACHE = "usergroup_cache";
  private static final String GROUP_ID = "3684a786-6671-4268-8ed0-9db82ebca60b";
  private static final String TENANT_ONE = "tenant_one";
  private static final String TENANT_TWO = "tenant_two";

  @Autowired
  private UserGroupService userGroupService;
  @Autowired
  private CacheManager cacheManager;
  @Autowired
  private AtomicReference<String> tenantId;
  @MockitoBean
  private UserClient userClient;

  @BeforeEach
  void setUp() {
    Objects.requireNonNull(cacheManager.getCache(USERGROUP_CACHE)).clear();
  }

  @Test
  void getUserGroupById_shouldReturnUserGroup() {
    var expectedUserGroup = new UserGroup().id(GROUP_ID).group("undergraduate");
    when(userClient.getGroupById(GROUP_ID)).thenReturn(expectedUserGroup);

    var actualUserGroup = getUserGroupById(TENANT_ONE);

    assertEquals(expectedUserGroup, actualUserGroup);
    verify(userClient).getGroupById(GROUP_ID);
  }

  @Test
  void getUserGroupById_shouldCacheUserGroupsByTenantAndGroupId() {
    var tenantOneUserGroup = new UserGroup().id(GROUP_ID).group("undergraduate");
    var tenantTwoUserGroup = new UserGroup().id(GROUP_ID).group("graduate");
    when(userClient.getGroupById(GROUP_ID)).thenReturn(tenantOneUserGroup, tenantTwoUserGroup);

    assertEquals(tenantOneUserGroup, getUserGroupById(TENANT_ONE));
    assertEquals(tenantOneUserGroup, getUserGroupById(TENANT_ONE));
    assertEquals(tenantTwoUserGroup, getUserGroupById(TENANT_TWO));
    assertEquals(tenantOneUserGroup, getUserGroupById(TENANT_ONE));

    verify(userClient, times(2)).getGroupById(GROUP_ID);
  }

  private UserGroup getUserGroupById(String tenantId) {
    this.tenantId.set(tenantId);
    return userGroupService.getUserGroupById(GROUP_ID);
  }

  @EnableCaching
  @TestConfiguration
  static class TestContextConfiguration {

    @Bean
    CacheManager cacheManager() {
      return new ConcurrentMapCacheManager(USERGROUP_CACHE);
    }

    @Bean
    AtomicReference<String> tenantId() {
      return new AtomicReference<>();
    }

    @Bean
    FolioExecutionContext folioExecutionContext(AtomicReference<String> tenantId) {
      return new FolioExecutionContext() {
        @Override
        public String getTenantId() {
          return tenantId.get();
        }
      };
    }
  }
}
