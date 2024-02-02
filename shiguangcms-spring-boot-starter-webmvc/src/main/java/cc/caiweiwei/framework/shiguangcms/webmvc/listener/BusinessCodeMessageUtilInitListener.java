package cc.caiweiwei.framework.shiguangcms.webmvc.listener;

import cc.caiweiwei.framework.shiguangcms.common.utils.BusinessCodeMessageUtil;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * 描述：业务码消息工具初始化监听器
 *
 * @author CaiZhengwei
 * @since 2024/1/2 22:48
 */
@Component
public class BusinessCodeMessageUtilInitListener implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        MessageSource messageSource = event.getApplicationContext().getBean("messageSource", MessageSource.class);
        String serviceName = event.getApplicationContext().getEnvironment().getProperty("spring.application.name");
        BusinessCodeMessageUtil.init(messageSource, serviceName);
    }

}
