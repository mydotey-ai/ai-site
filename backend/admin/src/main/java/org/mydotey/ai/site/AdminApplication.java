package org.mydotey.ai.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Admin Application
 *
 * @author AI-Site
 */
@SpringBootApplication(scanBasePackages = "org.mydotey.ai.site")
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}