package org.folio.edge.users.controller;

import org.folio.edge.users.service.UsersService;
import org.folio.users.domain.dto.PatronPinWithId;
import org.folio.users.domain.dto.RequestQueryParameters;
import org.folio.users.domain.dto.UserResults;
import org.folio.users.domain.dto.Userdata;
import org.folio.users.rest.resource.UsersApi;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Edge users")
@Log4j2
@RequiredArgsConstructor
@RestController
public class UsersController implements UsersApi {

  private final UsersService usersService;

  @Override
  public ResponseEntity<Userdata> createUser(String xOkapiTenant, String xOkapiToken, String lang, Userdata userdata) {
    log.info("Create user with user data: {}", userdata);
    var createdUser = usersService.createUser(lang, userdata);
    return ResponseEntity.ok(createdUser);
  }

  @Override
  public ResponseEntity<Void> userExists(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters queryParameters) {
    log.debug("Find user by query: {}", queryParameters::getQuery);
    boolean userExists = usersService.usersExistsByQuery(queryParameters);
    return userExists ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }

  @Override
  public ResponseEntity<UserResults> getUsers(String xOkapiTenant, String xOkapiToken,
      RequestQueryParameters queryParameters) {
    log.debug("Get users by query: {}", queryParameters::getQuery);
    return ResponseEntity.ok(usersService.getUsers(queryParameters));
  }

  @Override
  public ResponseEntity<Void> setPin(String xOkapiTenant, String xOkapiToken, PatronPinWithId patronPin) {
    usersService.setPin(patronPin);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Override
  public ResponseEntity<Void> verifyPin(String xOkapiTenant, String xOkapiToken, PatronPinWithId patronPin) {
    usersService.verifyPin(patronPin);
    return ResponseEntity.ok().build();
  }
}
