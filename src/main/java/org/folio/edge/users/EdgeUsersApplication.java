package org.folio.edge.users;

import lombok.extern.log4j.Log4j2;
import org.folio.common.utils.tls.FipsChecker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Log4j2
public class EdgeUsersApplication {

  public static void main(String[] args) {
    log.info(FipsChecker.getFipsChecksResultString());
    SpringApplication.run(EdgeUsersApplication.class, args);
  }

}
