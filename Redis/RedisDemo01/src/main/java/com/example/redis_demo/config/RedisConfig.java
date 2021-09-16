package com.example.redis_demo.config;

import com.example.redis_demo.subscriber.Receiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
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

//    @Bean
//    //Cho primary vì ứng dụng báo lỗi có 2 bean. Bean còn lại là do Redis tạo tự động thì phải. Cái này dùng khi làm CRUD
//    @Primary
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
//        template.setValueSerializer(new JdkSerializationRedisSerializer());
//        template.setEnableTransactionSupport(true);
//        template.afterPropertiesSet();
//        return template;
//    }

    // Hai kiểu config redisTemplate() nay chạy thử đều thấy ok, CRUD vẫn chạy, message vẫn chạy

    @Bean
    //Cho primary vì ứng dụng báo lỗi có 2 bean. Bean còn lại là do Redis tạo tự động thì phải.
    // Ko biết có ảnh hưởng gì hay ko, cái này dùng để cho message pubsub
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("pubsub:first-channel");
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(new Receiver());
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.addMessageListener(messageListenerAdapter(), topic());
        return container;
    }
}
