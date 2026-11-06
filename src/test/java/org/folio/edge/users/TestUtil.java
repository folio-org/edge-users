package org.folio.edge.users;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

@Log4j2
public class TestUtil {

  public static final JsonMapper OBJECT_MAPPER = JsonMapper.builder()
      .changeDefaultPropertyInclusion(inclusion -> inclusion
          .withValueInclusion(Include.NON_NULL)
          .withContentInclusion(Include.NON_NULL))
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
      .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
      .build();

  public static String readFileContentFromResources(String path) {
    try {
      ClassLoader classLoader = TestUtil.class.getClassLoader();
      URL url = classLoader.getResource(path);
      return IOUtils.toString(url, StandardCharsets.UTF_8);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new IllegalStateException(e);
    }
  }

}
