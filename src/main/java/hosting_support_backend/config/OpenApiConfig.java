package hosting_support_backend.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI jwtDemoOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("JWT Demo API")
                        .description("Spring Boot + Spring Security JWT authentication demo")
                        .version("v1"))
                // Declares "bearerAuth" as a security scheme: a JWT sent as
                // "Authorization: Bearer <token>".
                .components(new Components().addSecuritySchemes(
                        BEARER_SCHEME_NAME,
                        new SecurityScheme()
                                .name(BEARER_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                ))
                // Applies that scheme to every endpoint by default, so Swagger UI
                // shows a lock icon and sends the header once you click "Authorize".
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME_NAME));
    }
}