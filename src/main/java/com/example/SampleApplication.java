package com.example;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Header;

import com.rabbitmq.client.Channel;

@SpringBootApplication
@EnableRabbit
public class SampleApplication {

	public static void main(String[] args) throws Exception {
		 SpringApplication.run(SampleApplication.class, args);
		//context.getBean(RabbitTemplate.class).convertAndSend("", "sample", "foo");
		
		//context.close();
	}

	@Bean
	public Queue sample() {
		return new Queue("sample");
	}

	@Bean
	
	public Listener listener() {
		return new Listener();
	}
	
//	@Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
//           Listener listenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames("sample");
//        container.setConcurrentConsumers(2);
//        return container;
//    }
	 @Bean
	    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory rabbitConnectionFactory) {
	        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
	        factory.setConnectionFactory(rabbitConnectionFactory);
	        //factory.setConcurrentConsumers(3);
	        //factory.setMaxConcurrentConsumers(10);
	       return factory;
	    }
	
	public static class Listener {

		//private final CountDownLatch latch = new CountDownLatch(1);

		@RabbitListener(queues = "sample")
		public void receive(String payload, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag)
				throws IOException {
			System.out.println(payload);
			System.out.println("Consumer is going to sleep for 20 seconds");
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("consumer completed execution goin to acknowledge");
			channel.basicAck(tag, true);
			//latch.countDown();
		}

	}

}
