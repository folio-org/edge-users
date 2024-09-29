package org.folio.edge.users;

import lombok.extern.log4j.Log4j2;
import org.folio.common.utils.tls.FipsChecker;
import org.folio.spring.cql.JpaCqlConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, JpaCqlConfiguration.class})
@EnableFeignClients
@Log4j2
public class EdgeUsersApplication {

  public static void main(String[] args) {
    log.info(FipsChecker.getFipsChecksResultString());
    SpringApplication.run(EdgeUsersApplication.class, args);
  }

}
