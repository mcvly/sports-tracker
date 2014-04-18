package org.mcvly.tracker.config;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.mcvly.tracker.service.CustomDataSetLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author mcvly
 * @since 17.04.14
 */

@Configuration
@Import(ServiceConfig.class) // persistence settings
@DbUnitConfiguration(dataSetLoader = CustomDataSetLoader.class) // custom dbunit loader
@DatabaseSetup("classpath:/sql/") // csv dir
public class ServiceTestConfiguration {

        public static void main(String[] args) {
            SpringApplication app = new SpringApplication(ServiceTestConfiguration.class);
            app.setShowBanner(false);
            app.run(args);
        }
}
