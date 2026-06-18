# Environment Variables

This table documents the operator-facing environment variables declared by `edge-users`.
Generic Spring Boot environment variables are supported by the framework and are not exhaustively listed here.

| Name | Default | Required | Description |
| --- | --- | --- | --- |
| `JAVA_OPTIONS` | `-XX:MaxRAMPercentage=66.0` | false | Java runtime options for the module container. |
| `SERVER_PORT` | `8080` | false | HTTP port used by the embedded Spring Boot server. |
| `FOLIO_ENVIRONMENT` | `dev` | false | FOLIO environment name used by folio-spring-system-user configuration. |
| `FOLIO_CLIENT_OKAPIURL` |  | true | Okapi base URL used for outbound FOLIO service calls. This must be set to the reachable Okapi URL for the deployment environment. |
| `FOLIO_CLIENT_TLS_ENABLED` | `false` | false | Enables custom TLS truststore configuration for outbound Okapi/client calls. |
| `FOLIO_CLIENT_TLS_TRUSTSTOREPATH` |  | false | Path to the outbound client TLS truststore. Required only when `FOLIO_CLIENT_TLS_ENABLED` is true and the server certificate is not trusted by the default JVM truststore. |
| `FOLIO_CLIENT_TLS_TRUSTSTOREPASSWORD` |  | false | Password for the outbound client TLS truststore. |
| `FOLIO_CLIENT_TLS_TRUSTSTORETYPE` |  | false | Type of the outbound client TLS truststore, for example `JKS`, `PKCS12`, or `BCFKS`. If unset, the JVM default truststore type is used. |
| `SECURE_STORE_PROPS` | `src/main/resources/ephemeral.properties` | false | Path or URL to the edge secure-store properties file used to configure the secure store for institutional user credentials. |
| `API_KEY_SOURCES` | `PARAM,HEADER,PATH` | false | Comma-separated list of locations where the edge API key is accepted. Valid values are `PARAM`, `HEADER`, and `PATH`. |
| `TOKEN_CACHE_TTL_MS` | `3600000` | false | Time to live, in milliseconds, for successful Okapi token cache entries. |
| `NULL_TOKEN_CACHE_TTL_MS` | `30000` | false | Time to live, in milliseconds, for failed or null Okapi token cache entries. |
| `TOKEN_CACHE_CAPACITY` | `100` | false | Maximum number of Okapi token entries held by the in-memory edge token cache. |
| `EDGE_SECURITY_FILTER_ENABLED` | `true` | false | Enables the edge API key security filter. |
| `HEADER_EDGE_VALIDATION_EXCLUDE` | `/admin/health,/admin/info,/swagger-resources,/v2/api-docs,/swagger-ui,/_/tenant` | false | Comma-separated list of request path prefixes excluded from edge API key validation. |

## Framework Variables

The module also runs on Spring Boot and supports standard Spring environment variables through framework relaxed binding.
Examples include `SPRING_PROFILES_ACTIVE`, `SERVER_SSL_BUNDLE`, and `SPRING_SSL_BUNDLE_JKS_*` variables for TLS bundles.
