package edu.whut.cs.jee.mooc;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class MoocClassMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoocClassMonitorApplication.class, args);
    }

}
