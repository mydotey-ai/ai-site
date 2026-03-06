package org.mydotey.ai.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AI-Site Application
 *
 * @author AI-Site
 */
@SpringBootApplication(scanBasePackages = "org.mydotey.ai.site")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}