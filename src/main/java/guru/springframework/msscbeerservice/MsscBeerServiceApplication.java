package guru.springframework.msscbeerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import reactor.core.publisher.Hooks;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"guru.sfg.beer.common", "guru.springframework.msscbeerservice"})
public class MsscBeerServiceApplication {

    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
        SpringApplication.run(MsscBeerServiceApplication.class, args);
    }

}
