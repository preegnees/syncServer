package ru.radmir.synServer.synServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.radmir.synServer.synServer.init.Init;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SynServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SynServerApplication.class, args);
    }
}
