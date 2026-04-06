package org.folio.edge.users.client;

import org.folio.users.domain.dto.AutomatedPatronBlockResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(contentType = "application/json")
public interface PatronBlocksClient {

  @GetExchange(value = "automated-patron-blocks/{userId}")
  AutomatedPatronBlockResponse getAutomatedPatronBlocks(@PathVariable String userId);

}
