package org.folio.edge.users;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.folio.edgecommonspring.client.EdgeClientProperties;
import org.folio.spring.integration.XOkapiHeaders;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

@Log4j2
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class BaseIntegrationTests {

  private static final String OKAPI_URL_FIELD = "okapiUrl";

  protected static final WireMockServer WIRE_MOCK = new WireMockServer(
      options()
          .dynamicPort()
          .templatingEnabled(true)
          .globalTemplating(false));

  @Autowired
  protected MockMvc mockMvc;

  @BeforeAll
  static void beforeAll(@Autowired EdgeClientProperties edgeClientProperties) {
    WIRE_MOCK.start();
    ReflectionTestUtils.setField(edgeClientProperties, OKAPI_URL_FIELD, WIRE_MOCK.baseUrl());
    log.info("Wire mock started");
  }

  @AfterAll
  static void afterAll() {
    WIRE_MOCK.stop();
  }

  @SneakyThrows
  protected static ResultActions doPost(MockMvc mockMvc, String url, String paramName, String paramValue,
      String bodyContent) {
    return mockMvc.perform(post(url)
        .content(bodyContent)
        .param(paramName, paramValue)
        .headers(defaultHeadersWithAuthorization()));
  }

  @SneakyThrows
  protected static ResultActions doPost(MockMvc mockMvc, String url, String paramName, String paramValue,
      String bodyContent, String apiKey) {
    HttpHeaders httpHeaders = defaultHeaders();
    httpHeaders.put(XOkapiHeaders.AUTHORIZATION, List.of(apiKey));
    return mockMvc.perform(post(url)
        .content(bodyContent)
        .param(paramName, paramValue)
        .headers(httpHeaders));
  }

  protected static HttpHeaders defaultHeadersWithAuthorization() {
    final HttpHeaders httpHeaders = defaultHeaders();
    httpHeaders.put(XOkapiHeaders.AUTHORIZATION, List.of(TestConstant.TEST_API_KEY));
    return httpHeaders;
  }

  protected static HttpHeaders defaultHeaders() {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(APPLICATION_JSON);
    httpHeaders.put(XOkapiHeaders.TENANT, List.of(TestConstant.TEST_TENANT));
    httpHeaders.put(XOkapiHeaders.URL, List.of(WIRE_MOCK.baseUrl()));
    return httpHeaders;
  }

}
