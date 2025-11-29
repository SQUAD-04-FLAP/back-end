package dev.squad04.projetoFlap.auth.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI getOpenAPI() {

        Contact contact = new Contact();
        contact.setName("Squad04-Flap");
        contact.email("squad04flapdigital@gmail.com");

        Info info = new Info();
        info.title("FlapBoard");
        info.version("v1");
        info.description("Aplicação para gerenciamento de tarefas e projetos");
        info.contact(contact);

        return new OpenAPI()
                .info(info)
                .servers(List.of(
                        new Server().url("https://api.flapkanban.top").description("Servidor de Produção"),
                        new Server().url("http://ec2-54-226-167-245.compute-1.amazonaws.com:8080").description("Servidor de desenvolvimento"),
                        new Server().url("http://localhost:8080").description("Servidor Local")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .name("Bearer Authentication")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
