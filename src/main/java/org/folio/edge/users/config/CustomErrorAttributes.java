package org.folio.edge.users.config;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

  private static final String DEFAULT_KEY_STATUS = "status";
  private static final String DEFAULT_KEY_MESSAGE = "message";
  private static final String KEY_STATUS = "code";
  private static final String KEY_MESSAGE = "errorMessage";

  @Override
  public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {

    Map<String, Object> defaultErrorAttributes = super.getErrorAttributes(webRequest, options);

    Map<String, Object> errorAttributes = new LinkedHashMap<>();
    errorAttributes.put(KEY_STATUS, defaultErrorAttributes.get(DEFAULT_KEY_STATUS));
    errorAttributes.put(KEY_MESSAGE, defaultErrorAttributes.get(DEFAULT_KEY_MESSAGE));

    return errorAttributes;
  }

}
