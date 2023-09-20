package cn.kdjlyy.elasticsearchdemo.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/*
Swagger 访问地址/swagger-ui/index.html，如：http://localhost:8081/swagger-ui/index.html
bootstrap-ui 访问地址/doc.html，如：http://localhost:8081/doc.html
 */

@EnableOpenApi
@Configuration
public class Swagger3Config {
    @Value("${swagger.enabled}")
    Boolean swaggerEnabled;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
//            .securityContexts(securityContexts())
//            .securitySchemes(securitySchemes())
                .apiInfo(builderApiInfo())
                .enable(swaggerEnabled)
                .select()
                // 扫描所有带有 @ApiOperation 注解的类
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 扫描所有的 controller
                // .apis(RequestHandlerSelectors.basePackage("com.shenlanbao.product.library.management.controller"))
                .paths(PathSelectors.any())
                // 不显示默认的错误页面接口
                .paths(PathSelectors.regex("/error").negate())
                .build();
    }

    private ApiInfo builderApiInfo() {
        return new ApiInfoBuilder()
                .contact(new Contact(
                                "ElasticsearchDemo",
                                "127.0.0.1:8765",
                                "kdjlyy@qq.com"
                        )
                )
                .title("ElasticsearchDemo")
                .description("ElasticsearchDemo接口文档")
                .version("1.0")
                .build();
    }

//    // 配置请求头 token
//    private List<SecurityContext> securityContexts(){
//        return Arrays.asList(SecurityContext.builder()
//                .securityReferences(Arrays.asList(SecurityReference.builder()
//                        .reference("token")
//                        .scopes(new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")})
//                        .build())).build());
//    }
//
//    //配置请求头 token 参数
//    private List<SecurityScheme> securitySchemes(){
//        return Arrays.asList(new ApiKey("token凭证", "token", "header"));
//    }
}
