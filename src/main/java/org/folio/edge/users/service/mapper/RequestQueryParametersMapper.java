package org.folio.edge.users.service.mapper;

import java.util.LinkedHashMap;
import java.util.Map;
import org.folio.users.domain.dto.RequestQueryParameters;
import org.springframework.stereotype.Component;

@Component
public class RequestQueryParametersMapper {

  public Map<String, Object> toMap(RequestQueryParameters params) {
    Map<String, Object> map = new LinkedHashMap<>();
    if (params == null) {
      return map;
    }

    if (params.getQuery() != null && !params.getQuery().isBlank()) {
      map.put("query", params.getQuery());
    }
    if (params.getLimit() != null) {
      map.put("limit", params.getLimit());
    }
    if (params.getOffset() != null) {
      map.put("offset", params.getOffset());
    }
    if (params.getLang() != null) {
      map.put("lang", params.getLang());
    }

    return map;
  }
}

