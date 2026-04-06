package org.folio.edge.users.client;

import java.util.Map;
import org.folio.users.domain.dto.PatronPinWithId;
import org.folio.users.domain.dto.UserGroup;
import org.folio.users.domain.dto.UserResults;
import org.folio.users.domain.dto.Userdata;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(contentType = "application/json")
public interface UserClient {

  @PostExchange(value = "users")
  Userdata createUser(@RequestParam String lang, @RequestBody Userdata userdata);

  @GetExchange(value = "users")
  UserResults findUsers(@RequestParam Map<String, ?> requestQueryParameters);

  @PostExchange(value = "patron-pin")
  void setPin(@RequestBody PatronPinWithId userdata);

  @PostExchange(value = "patron-pin/verify")
  void verifyPin(@RequestBody PatronPinWithId userdata);

  @GetExchange(value = "groups/{id}")
  UserGroup getGroupById(@PathVariable String id);
}
