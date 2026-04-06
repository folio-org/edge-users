package org.folio.edge.users.config;

import lombok.RequiredArgsConstructor;
import org.folio.edge.users.client.UserClient;
import org.folio.edge.users.client.FeesFinesClient;
import org.folio.edge.users.client.PatronBlocksClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class HttpClientConfiguration {

  @Qualifier("edgeHttpServiceProxyFactory")
  private final HttpServiceProxyFactory httpServiceProxyFactory;

  @Bean
  public UserClient userClient() {
    return httpServiceProxyFactory.createClient(UserClient.class);
  }

  @Bean
  public FeesFinesClient feesFinesClient() {
    return httpServiceProxyFactory.createClient(FeesFinesClient.class);
  }

  @Bean
  public PatronBlocksClient patronBlocksClient() {
    return httpServiceProxyFactory.createClient(PatronBlocksClient.class);
  }
}

