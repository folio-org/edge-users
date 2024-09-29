package org.folio.edge.users.client;

import org.folio.edge.users.config.FolioClientConfig;
import org.folio.users.domain.dto.ManualBlocksResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "folio-feesfines", configuration = FolioClientConfig.class)
public interface FeesFinesClient {

  @GetMapping(value = "/manualblocks")
  ManualBlocksResponse getManualBlocks(@SpringQueryMap Object requestQueryParameters);

}
