package org.mydotey.ai.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Service Application
 *
 * Unified service module for AI-Site (public API + admin)
 *
 * @author AI-Site
 */
@SpringBootApplication(scanBasePackages = "org.mydotey.ai.site")
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}