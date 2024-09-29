package org.folio.edge.users;

import org.folio.edge.users.config.UsersClientRequestInterceptor;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.folio.edgecommonspring.client.EdgeFeignClientProperties;
import org.folio.edgecommonspring.client.EnrichUrlClient;
import org.folio.spring.integration.XOkapiHeaders;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Log4j2
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@Import({UsersClientRequestInterceptor.class})
@AutoConfigureMockMvc
@AutoConfigureObservability
public abstract class BaseIntegrationTests {

  private static final String OKAPI_URL = "okapiUrl";

  protected static final WireMockServer WIRE_MOCK = new WireMockServer(
      WireMockSpring.options()
          .dynamicPort()
          .extensions(new ResponseTemplateTransformer(false)));

  @Autowired
  protected MockMvc mockMvc;

  @BeforeAll
  static void beforeAll(@Autowired EnrichUrlClient enrichUrlClient,
                        @Autowired UsersClientRequestInterceptor interceptor,
                        @Autowired EdgeFeignClientProperties properties) {
    WIRE_MOCK.start();
    var url = WIRE_MOCK.baseUrl();
    ReflectionTestUtils.setField(enrichUrlClient, OKAPI_URL, url);
    ReflectionTestUtils.setField(interceptor, OKAPI_URL, url);
    ReflectionTestUtils.setField(properties, OKAPI_URL, url);
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
