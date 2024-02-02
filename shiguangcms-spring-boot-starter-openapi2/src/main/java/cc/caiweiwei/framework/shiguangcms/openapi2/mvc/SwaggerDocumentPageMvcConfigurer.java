package cc.caiweiwei.framework.shiguangcms.openapi2.mvc;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;

@Component
public class SwaggerDocumentPageMvcConfigurer implements WebMvcConfigurer {


    @Override
    public void addViewControllers(@Nonnull ViewControllerRegistry registry) {
        //转向Swagger文档页。
        registry.addRedirectViewController("/", "/doc.html");
    }
}
