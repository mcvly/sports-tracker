package org.mcvly.tracker.model.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mcvly
 * @since 16.04.14
 */
@Configuration
@EnableJpaRepositories("org.mcvly.tracker.model.repository")
@EnableTransactionManagement
public class PersistenceConfig {

    @Bean(name = {"org.springframework.boot.autoconfigure.AutoConfigurationUtils.basePackages"})
    public List<String> getBasePackages() {
        List<String> basePackages = new ArrayList<>();
        basePackages.add("org.mcvly.tracker.core");
        return basePackages;
    }

}
