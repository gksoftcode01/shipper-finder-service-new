package ai.yarmook.shipperfinder.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories({ "ai.yarmook.shipperfinder.core.repository" })
public class CommonConfig {}
