package org.folio.edge.users.service;

import org.folio.edge.users.client.FeesFinesClient;
import org.folio.edge.users.client.PatronBlocksClient;
import org.folio.edge.users.client.UsersClient;
import org.folio.users.domain.dto.AutomatedPatronBlock;
import org.folio.users.domain.dto.ManualBlock;
import org.folio.users.domain.dto.PatronPinWithId;
import org.folio.users.domain.dto.RequestQueryParameters;
import org.folio.users.domain.dto.UserGroup;
import org.folio.users.domain.dto.UserResults;
import org.folio.users.domain.dto.Userdata;
import java.util.Objects;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UsersService {

  private static final Predicate<AutomatedPatronBlock> automatedBorrowingPredicate = AutomatedPatronBlock::getBlockBorrowing;
  private static final Predicate<AutomatedPatronBlock> automatedRenewalsPredicate = AutomatedPatronBlock::getBlockRenewals;
  private static final Predicate<AutomatedPatronBlock> automatedRequestsPredicate = AutomatedPatronBlock::getBlockRequests;
  private static final Predicate<ManualBlock> manualBorrowingPredicate = ManualBlock::getBorrowing;
  private static final Predicate<ManualBlock> manualRenewalsPredicate = ManualBlock::getRenewals;
  private static final Predicate<ManualBlock> manualRequestsPredicate = ManualBlock::getRequests;

  private final UsersClient usersClient;
  private final FeesFinesClient feesFinesClient;
  private final PatronBlocksClient patronBlocksClient;

  public Userdata createUser(String lang, Userdata userdata) {
    return usersClient.createUser(lang, userdata);
  }

  public boolean usersExistsByQuery(RequestQueryParameters queryParameters) {
    var users = usersClient.findUsers(queryParameters);
    return users.getTotalRecords() > 0;
  }

  public UserResults getUsers(RequestQueryParameters queryParameters) {
    log.debug("Get users with query parameters '{}'", queryParameters);
    final UserResults userResults = usersClient.findUsers(queryParameters);
    return addAdditionalUserAttributes(userResults);
  }

  private UserResults addAdditionalUserAttributes(final UserResults userResults) {
    populateBlockedAttribute(userResults);
    return populateUserGroupName(userResults);
  }

  @Cacheable(value = "usergroup_cache", key = "{ #groupId }")
  public UserGroup getUserGroupById(final String groupId) {
    log.debug("Get user group by id '{}'", groupId);
    return usersClient.getGroupById(groupId);
  }

  private void populateBlockedAttribute(UserResults userResults) {
    userResults.getUsers().forEach(userdata -> {
      String userId = userdata.getId();
      var isUserBlocked = hasManualBlocks(userId);
      if (!isUserBlocked) {
        isUserBlocked = hasUserAutomatedBlocks(userId);
      }
      userdata.blocked(isUserBlocked);
      log.info("User with id {} has blocked status: {}", userId, isUserBlocked);
    });
  }

  private UserResults populateUserGroupName(final UserResults userResults) {
    userResults.getUsers().stream()
      .filter(userdata -> Objects.nonNull(userdata.getPatronGroup()))
      .forEach(userdata -> {
        final UserGroup userGroup = getUserGroupById(userdata.getPatronGroup());
        log.debug("Retrieved user group by id '{}': '{}'", userdata.getPatronGroup(), userGroup);
        userdata.setPatronGroupName(userGroup.getGroup());
      });
    return userResults;
  }

  private boolean hasManualBlocks(String userId) {
    var requestQueryParameters = new RequestQueryParameters();
    requestQueryParameters.setQuery(String.format("userId=%s", userId));
    var blocksResponse = feesFinesClient.getManualBlocks(requestQueryParameters);
    var blocks = blocksResponse.getManualblocks();
    return blocks.stream()
        .anyMatch(manualBorrowingPredicate
            .or(manualRenewalsPredicate)
            .or(manualRequestsPredicate));
  }

  private boolean hasUserAutomatedBlocks(String userId) {
    var automatedPatronBlocksResponse = patronBlocksClient.getAutomatedPatronBlocks(userId);
    var automatedPatronBlocks = automatedPatronBlocksResponse.getAutomatedPatronBlocks();
    return automatedPatronBlocks.stream()
        .anyMatch(automatedBorrowingPredicate
            .or(automatedRenewalsPredicate)
            .or(automatedRequestsPredicate));
  }

  public void setPin(PatronPinWithId patronPin) {
    usersClient.setPin(patronPin);
  }

  public void verifyPin(PatronPinWithId patronPin) {
    usersClient.verifyPin(patronPin);
  }
}
