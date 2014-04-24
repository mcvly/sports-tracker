package org.mcvly.tracker.controller.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author <a href="mailto:RMalyona@luxoft.com">Ruslan Malyona</a>
 * @since 18.04.2014
 */
@Configuration
@Import(ServiceConfig.class)
public class WebConfig {

}
