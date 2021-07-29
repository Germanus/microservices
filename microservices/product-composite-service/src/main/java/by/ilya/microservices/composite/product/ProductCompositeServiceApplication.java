package by.ilya.microservices.composite.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@OpenAPIDefinition
@SpringBootApplication
@ComponentScan("by.ilya")
public class ProductCompositeServiceApplication implements WebFluxConfigurer {

	@Value("${api.common.version}")           String apiVersion;
    @Value("${api.common.title}")             String apiTitle;
    @Value("${api.common.description}")       String apiDescription;
    @Value("${api.common.termsOfServiceUrl}") String apiTermsOfServiceUrl;
    @Value("${api.common.license}")           String apiLicense;
    @Value("${api.common.licenseUrl}")        String apiLicenseUrl;
    @Value("${api.common.contact.name}")      String apiContactName;
    @Value("${api.common.contact.url}")       String apiContactUrl;
    @Value("${api.common.contact.email}")     String apiContactEmail;

	/**
	 * Will exposed on $HOST:$PORT/swagger-ui.html
	 *
	 * @return
	 */
	// @Bean
	// public Docket apiDocumentation() {

	// 	return new Docket(SWAGGER_2)
	// 		.select()
	// 		.apis(basePackage("by.ilya.microservices.composite.product"))
	// 		.paths(PathSelectors.any())
	// 		.build()
    //             .globalResponses(GET, emptyList())
	// 			.apiInfo(new ApiInfo(
    //                 apiTitle,
    //                 apiDescription,
    //                 apiVersion,
    //                 apiTermsOfServiceUrl,
    //                 new Contact(apiContactName, apiContactUrl, apiContactEmail),
    //                 apiLicense,
    //                 apiLicenseUrl,
    //                 emptyList()
    //             ));
    // }

	// @Bean
    // public OpenAPI customOpenAPI() {
    //  return new OpenAPI()
    //       .info(new Info()
    //       .title("sample application API")
    //       .version("1.0.0")
    //       .description("Description")
    //       .termsOfService("http://swagger.io/terms/")
    //       .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    // }

	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(ProductCompositeServiceApplication.class, args);
	}


	// @Override
	// public void addResourceHandlers(ResourceHandlerRegistry registry) {
	// 	registry.addResourceHandler("/swagger-ui.html**")
	// 	.addResourceLocations("classpath:/META-INF/resources");
	// }

}
