package cc.caiweiwei.framework.shiguangcms.openapi2.config;

import com.github.xiaoymin.knife4j.spring.configuration.Knife4jInfoProperties;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jProperties;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@EnableSwagger2WebMvc
@EnableConfigurationProperties({Knife4jProperties.class})
public class SwaggerConfiguration {

    @Value("${spring.application.name}")
    private String applicationName;

    private final Knife4jProperties knife4jProperties;

    public SwaggerConfiguration(Knife4jProperties knife4jProperties) {
        this.knife4jProperties = knife4jProperties;
    }


    @ConditionalOnMissingBean
    @Bean
    public Docket api(ApiInfo apiInfo) {
        List<ResponseMessage> responseMessages = new ArrayList<>();
        responseMessages.add(new ResponseMessageBuilder().code(200).message("OK").build());
        responseMessages.add(new ResponseMessageBuilder().code(201).message("Created").build());
        responseMessages.add(new ResponseMessageBuilder().code(401).message("Unauthorized").build());
        responseMessages.add(new ResponseMessageBuilder().code(403).message("Forbidden").build());
        responseMessages.add(new ResponseMessageBuilder().code(404).message("Not Found").build());
        responseMessages.add(new ResponseMessageBuilder().code(500).message("Internal Server Error").build());
        responseMessages.add(new ResponseMessageBuilder().code(502).message("Bad Gateway").build());

        List<Parameter> operationParameters = new ArrayList<>();
        operationParameters.add(new ParameterBuilder()
                .name("Authorization")
                .description("认证token头")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build());
        operationParameters.add(new ParameterBuilder()
                .name("UserId")
                .description("用户ID")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build());


        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo)
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages)
                .globalOperationParameters(operationParameters);
    }

    @ConditionalOnMissingBean
    @Bean
    public ApiInfo apiInfo() {
        Knife4jInfoProperties openapi = knife4jProperties.getOpenapi();
        if (openapi != null) {
            return new ApiInfoBuilder().title(openapi.getTitle())
                    .description(openapi.getDescription())
                    .version(openapi.getVersion())
                    .termsOfServiceUrl(openapi.getTermsOfServiceUrl())
                    .contact(new Contact(openapi.getConcat(), openapi.getUrl(), openapi.getEmail())).build();
        }
        return new ApiInfoBuilder().title(applicationName)
                .description("时光CMS-API文档")
                .version("v1.0.0")
                .termsOfServiceUrl("https://cms.caiweiwei.cc")
                .contact(new Contact("蔡伟伟", "https://caiweiwei.cc", "caiweiwei@caiweiwei.cc")).build();
    }
}
