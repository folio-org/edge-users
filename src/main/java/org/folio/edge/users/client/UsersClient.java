package org.folio.edge.users.client;

import org.folio.edge.users.config.FolioClientConfig;
import org.folio.users.domain.dto.PatronPinWithId;
import org.folio.users.domain.dto.UserGroup;
import org.folio.users.domain.dto.UserResults;
import org.folio.users.domain.dto.Userdata;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "folio-users", configuration = FolioClientConfig.class)
public interface UsersClient {

  @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
  Userdata createUser(@RequestParam String lang, @RequestBody Userdata userdata);

  @GetMapping(value = "/users")
  UserResults findUsers(@SpringQueryMap Object requestQueryParameters);

  @PostMapping(value = "/patron-pin", consumes = MediaType.APPLICATION_JSON_VALUE)
  void setPin(@RequestBody PatronPinWithId userdata);

  @PostMapping(value = "/patron-pin/verify", consumes = MediaType.APPLICATION_JSON_VALUE)
  void verifyPin(@RequestBody PatronPinWithId userdata);

  @GetMapping(value = "/groups/{id}")
  UserGroup getGroupById(@PathVariable String id);
}
