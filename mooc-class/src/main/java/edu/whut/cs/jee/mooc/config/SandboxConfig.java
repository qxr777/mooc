package edu.whut.cs.jee.mooc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
@Profile("sandbox")
public class SandboxConfig {

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() {
        try {
            redisServer = new RedisServer(6379);
            redisServer.start();
            System.out.println("Embedded Redis started on port 6379");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
            System.out.println("Embedded Redis stopped");
        }
    }
}
