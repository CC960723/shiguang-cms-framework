package cc.caiweiwei.framework.shiguangcms.elasticsearch;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {ElasticSearchAutoConfiguration.class})
@AutoConfiguration
public class ElasticSearchAutoConfiguration {
}
