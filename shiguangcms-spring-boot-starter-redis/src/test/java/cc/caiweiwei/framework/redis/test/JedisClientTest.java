package cc.caiweiwei.framework.redis.test;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Connection;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * 描述：JedisClient测试类
 *
 * @author CaiZhengwei
 * @since 2024/2/24 21:14
 */
public class JedisClientTest {


    @Test
    public void testOne(){
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.66.201",6380));
        nodes.add(new HostAndPort("192.168.66.201",6381));
        nodes.add(new HostAndPort("192.168.66.201",6382));
        nodes.add(new HostAndPort("192.168.66.202",6380));
        nodes.add(new HostAndPort("192.168.66.202",6381));
        nodes.add(new HostAndPort("192.168.66.202",6382));
        nodes.add(new HostAndPort("192.168.66.203",6380));
        nodes.add(new HostAndPort("192.168.66.203",6381));
        nodes.add(new HostAndPort("192.168.66.203",6382));

        GenericObjectPoolConfig<Connection> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxIdle(5000);
        poolConfig.setMaxTotal(8);
        poolConfig.setMaxIdle(8);
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setMaxWait(Duration.ofMillis(5000));


        JedisCluster jedisCluster = new JedisCluster(nodes,poolConfig);
        System.out.println(jedisCluster.get("name"));

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //明天测试下pipeline。


        jedisCluster.close();

    }


}
