package org.folio.edge.users.client;

import org.folio.edge.users.config.FolioClientConfig;
import org.folio.users.domain.dto.AutomatedPatronBlockResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "patron-blocks", configuration = FolioClientConfig.class)
public interface PatronBlocksClient {

  @GetMapping(value = "/automated-patron-blocks/{userId}")
  AutomatedPatronBlockResponse getAutomatedPatronBlocks(@PathVariable String userId);

}
