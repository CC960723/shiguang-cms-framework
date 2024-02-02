package cc.caiweiwei.framework.shiguangcms.mybatis.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 描述：MyBatis分页插件
 *
 * @author CaiZhengwei
 * @since 2024/2/2 12:32
 */
@Intercepts({@Signature(type = Executor.class, method = "", args = {})})
public class SelectPageInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(SelectPageInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) {
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        //NOP,暂时先不做任何逻辑，后续可以增加

    }
}
