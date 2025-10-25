package be.susu.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("be.susu.web")
                .packagesToScan("be.susu.web.controller")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        String keycloakUrl = System.getenv().getOrDefault("KC_URL", "https://lemur-17.cloud-iam.com/auth");
        String realm = System.getenv().getOrDefault("KC_REALM", "noj");

        SecurityScheme scheme = new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows().password(new OAuthFlow()
                        .tokenUrl(keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token")));

        return new OpenAPI()
                .info(new Info().title("Susu API").version("v1"))
                .addSecurityItem(new SecurityRequirement().addList("keycloak"))
                .components(new io.swagger.v3.oas.models.Components().addSecuritySchemes("keycloak", scheme));
    }
}
