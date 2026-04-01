package org.folio.edge.users.client;

import java.util.Map;
import org.folio.users.domain.dto.ManualBlocksResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(contentType = "application/json")
public interface FeesFinesClient {

  @GetExchange(value = "manualblocks")
  ManualBlocksResponse getManualBlocks(@RequestParam Map<String, ?> requestQueryParameters);

}
