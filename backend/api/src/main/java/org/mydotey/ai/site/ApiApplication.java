package org.mydotey.ai.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Application
 *
 * @author AI-Site
 */
@SpringBootApplication(scanBasePackages = "org.mydotey.ai.site")
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}