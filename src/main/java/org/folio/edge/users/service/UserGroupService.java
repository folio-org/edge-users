package org.folio.edge.users.service;

import org.folio.edge.users.client.UserClient;
import org.folio.users.domain.dto.UserGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserGroupService {

  private final UserClient userClient;

  @Cacheable(value = "usergroup_cache", key = "{ #groupId }")
  public UserGroup getUserGroupById(final String groupId) {
    log.debug("Get user group by id '{}'", groupId);
    return userClient.getGroupById(groupId);
  }
}
