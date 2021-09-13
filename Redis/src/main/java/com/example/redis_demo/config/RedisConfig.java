package com.example.redis_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    //Với Redis, ta có 2 library Lettuce và Jedis để tích hợp cùng. Ở đây ta dùng Jedis(thêm dependency trong file pom)
    @Bean
    public JedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName("localhost");
        //port này được map từ một docker container. Container này map port 6379 của redis với prot 4025. Nếu chỉ khởi
        //chạy mà ko map thì ứng dụng vẫn chạy nhưng khi thực hiện thao tác thì báo time out.
        configuration.setPort(4025);
        return new JedisConnectionFactory(configuration);
    }

    @Bean
    //Cho primary vì ứng dụng báo lỗi có 2 bean. Bean còn lại là do Redis tạo tự động thì phải
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }
}
