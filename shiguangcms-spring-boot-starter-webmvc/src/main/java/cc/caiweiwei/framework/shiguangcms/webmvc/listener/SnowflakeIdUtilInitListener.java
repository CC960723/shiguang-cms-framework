package cc.caiweiwei.framework.shiguangcms.webmvc.listener;

import cc.caiweiwei.framework.shiguangcms.common.utils.SnowflakeIdUtil;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 描述：雪花ID工具初始化监听器
 *
 * @author CaiZhengwei
 * @since 2024/1/2 22:45
 */
@Component
public class SnowflakeIdUtilInitListener implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        SnowflakeIdUtil.SnowflakeIdGenerator snowflakeIdGenerator = event.getApplicationContext().getBean("snowflakeIdGenerator", SnowflakeIdUtil.SnowflakeIdGenerator.class);
        SnowflakeIdUtil.init(snowflakeIdGenerator);
    }

}
