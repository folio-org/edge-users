# edge-users
Copyright (C) 2018-2019 The Open Library Foundation

This software is distributed under the terms of the Apache License, Version 2.0. See the file "LICENSE" for more information.

# Introduction
Provides an ability to retrieve and create users information from FOLIO

# Overview
The purpose of this edge API is to expose read and write FOLIO users APIs for easy consumption by external systems.


# Security
See [edge-common-spring](https://github.com/folio-org/edge-common-spring)

## Requires Permissions

Institutional users should be granted the following permission in order to use this edge API:
- `"users.collection.get"`
- `""users.item.post""`

## Configuration

* See [edge-common](https://github.com/folio-org/edge-common) for a description of how configuration works.

### TLS Configuration for HTTP Endpoints

To configure Transport Layer Security (TLS) for HTTP endpoints in edge module, the following configuration parameters can be used. These parameters allow you to specify key and keystore details necessary for setting up TLS.

#### Configuration Parameters

1. **`spring.ssl.bundle.jks.web-server.key.password`**
- **Description**: Specifies the password for the private key in the keystore.
- **Example**: `spring.ssl.bundle.jks.web-server.key.password=SecretPassword`

2. **`spring.ssl.bundle.jks.web-server.key.alias`**
- **Description**: Specifies the alias of the key within the keystore.
- **Example**: `spring.ssl.bundle.jks.web-server.key.alias=localhost`

3. **`spring.ssl.bundle.jks.web-server.keystore.location`**
- **Description**: Specifies the location of the keystore file in the local file system.
- **Example**: `spring.ssl.bundle.jks.web-server.keystore.location=/some/secure/path/test.keystore.bcfks`

4. **`spring.ssl.bundle.jks.web-server.keystore.password`**
- **Description**: Specifies the password for the keystore.
- **Example**: `spring.ssl.bundle.jks.web-server.keystore.password=SecretPassword`

5. **`spring.ssl.bundle.jks.web-server.keystore.type`**
- **Description**: Specifies the type of the keystore. Common types include `JKS`, `PKCS12`, and `BCFKS`.
- **Example**: `spring.ssl.bundle.jks.web-server.keystore.type=BCFKS`

6. **`server.ssl.bundle`**
- **Description**: Specifies which SSL bundle to use for configuring the server. This parameter links to the defined SSL bundle, for example, `web-server`.
- **Example**: `server.ssl.bundle=web-server`

7. **`server.port`**
- **Description**: Specifies the port on which the server will listen for HTTPS requests.
- **Example**: `server.port=8443`

#### Example Configuration

To enable TLS for the edge module using the above parameters, you need to provide them as the environment variables. Below is an example configuration:

```properties
spring.ssl.bundle.jks.web-server.key.password=SecretPassword
spring.ssl.bundle.jks.web-server.key.alias=localhost
spring.ssl.bundle.jks.web-server.keystore.location=classpath:test/test.keystore.bcfks
spring.ssl.bundle.jks.web-server.keystore.password=SecretPassword
spring.ssl.bundle.jks.web-server.keystore.type=BCFKS

server.ssl.bundle=web-server
server.port=8443
```
Also, you can use the relaxed binding with the upper case format, which is recommended when using system environment variables.
```properties
SPRING_SSL_BUNDLE_JKS_WEBSERVER_KEY_PASSWORD=SecretPassword
SPRING_SSL_BUNDLE_JKS_WEBSERVER_KEY_ALIAS=localhost
SPRING_SSL_BUNDLE_JKS_WEBSERVER_KEYSTORE_LOCATION=classpath:test/test.keystore.bcfks
SPRING_SSL_BUNDLE_JKS_WEBSERVER_KEYSTORE_PASSWORD=SecretPassword
SPRING_SSL_BUNDLE_JKS_WEBSERVER_KEYSTORE_TYPE=BCFKS

SERVER_SSL_BUNDLE=web-server
SERVER_PORT=8443
```

### TLS Configuration for Feign HTTP Clients

To configure Transport Layer Security (TLS) for HTTP clients created using Feign annotations in the edge module, you can use the following configuration parameters. These parameters allow you to specify trust store details necessary for setting up TLS for Feign clients.

#### Configuration Parameters

1. **`folio.client.okapiUrl`**
- **Description**: Specifies the base URL for the Okapi service.
- **Example**: `folio.client.okapiUrl=https://okapi:443`

2. **`folio.client.tls.enabled`**
- **Description**: Enables or disables TLS for the Feign clients.
- **Example**: `folio.client.tls.enabled=true`

3. **`folio.client.tls.trustStorePath`**
- **Description**: Specifies the location of the trust store file.
- **Example**: `folio.client.tls.trustStorePath=classpath:/some/secure/path/test.truststore.bcfks`

4. **`folio.client.tls.trustStorePassword`**
- **Description**: Specifies the password for the trust store.
- **Example**: `folio.client.tls.trustStorePassword="SecretPassword"`

5. **`folio.client.tls.trustStoreType`**
- **Description**: Specifies the type of the trust store. Common types include `JKS`, `PKCS12`, and `BCFKS`.
- **Example**: `folio.client.tls.trustStoreType=bcfks`

#### Note
The `trustStorePath`, `trustStorePassword`, and `trustStoreType` parameters can be omitted if the server provides a public certificate.

#### Example Configuration

To enable TLS for Feign HTTP clients using the above parameters, you need to provide them as the environment variables. Below is an example configuration:

```properties
folio.client.okapiUrl=https://okapi:443
folio.client.tls.enabled=true
folio.client.tls.trustStorePath=classpath:test/test.truststore.bcfks
folio.client.tls.trustStorePassword=SecretPassword
folio.client.tls.trustStoreType=bcfks
```
Also, you can use the relaxed binding with the upper case format, which is recommended when using system environment variables.
```properties
FOLIO_CLIENT_OKAPIURL=https://okapi:443
FOLIO_CLIENT_TLS_ENABLED=true
FOLIO_CLIENT_TLS_TRUSTSTOREPATH=classpath:test/test.truststore.bcfks
FOLIO_CLIENT_TLS_TRUSTSTOREPASSWORD=SecretPassword
FOLIO_CLIENT_TLS_TRUSTSTORETYPE=bcfks
```