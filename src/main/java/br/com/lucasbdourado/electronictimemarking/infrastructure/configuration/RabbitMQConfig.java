package br.com.lucasbdourado.electronictimemarking.infrastructure.configuration;

import static br.com.lucasbdourado.electronictimemarking.infrastructure.configuration.RabbitMQConstants.MARKING_REGISTER_EXCHANGE;
import static br.com.lucasbdourado.electronictimemarking.infrastructure.configuration.RabbitMQConstants.MARKING_REGISTER_QUEUE;
import static br.com.lucasbdourado.electronictimemarking.infrastructure.configuration.RabbitMQConstants.MARKING_REGISTER_ROUTING_KEY;
import static br.com.lucasbdourado.electronictimemarking.infrastructure.configuration.RabbitMQConstants.REMINDER_EVENTS_EXCHANGE;
import static br.com.lucasbdourado.electronictimemarking.infrastructure.configuration.RabbitMQConstants.WORKDAY_REMINDER_PREFERENCE_CHANGED_QUEUE;
import static br.com.lucasbdourado.electronictimemarking.infrastructure.configuration.RabbitMQConstants.WORKDAY_REMINDER_PREFERENCE_CHANGED_ROUTING_KEY;

import br.com.lucasbdourado.electronictimemarking.infrastructure.messaging.consumer.WorkDayReminderPreferenceChangedEvent;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJavaTypeMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig
{

	@Bean
	public DirectExchange markingRegisterExchange()
	{
		return new DirectExchange(MARKING_REGISTER_EXCHANGE);
	}

	@Bean
	public Queue markingRegisterQueue()
	{
		return new Queue(MARKING_REGISTER_QUEUE, true);
	}

	@Bean
	public Binding markingRegisterBinding(Queue markingRegisterQueue,
		DirectExchange markingRegisterExchange)
	{
		return BindingBuilder.bind(markingRegisterQueue).to(markingRegisterExchange)
			.with(MARKING_REGISTER_ROUTING_KEY);
	}

	@Bean
	public TopicExchange reminderEventsExchange()
	{
		return new TopicExchange(REMINDER_EVENTS_EXCHANGE);
	}

	@Bean
	public Queue workDayReminderPreferenceChangedQueue()
	{
		return new Queue(WORKDAY_REMINDER_PREFERENCE_CHANGED_QUEUE, true);
	}

	@Bean
	public Binding workDayReminderPreferenceChangedBinding(
		Queue workDayReminderPreferenceChangedQueue, TopicExchange reminderEventsExchange)
	{
		return BindingBuilder.bind(workDayReminderPreferenceChangedQueue).to(reminderEventsExchange)
			.with(WORKDAY_REMINDER_PREFERENCE_CHANGED_ROUTING_KEY);
	}

	@Bean
	public JacksonJsonMessageConverter jsonMessageConverter()
	{
		JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
		DefaultClassMapper classMapper = new DefaultClassMapper();
		classMapper.setIdClassMapping(Map.of(
			"br.com.reminderbot.producer.WorkDayReminderPreferenceChangedEvent",
			WorkDayReminderPreferenceChangedEvent.class));
		converter.setClassMapper(classMapper);
		converter.setTypePrecedence(JacksonJavaTypeMapper.TypePrecedence.INFERRED);
		converter.setAlwaysConvertToInferredType(true);
		return converter;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
		JacksonJsonMessageConverter jsonMessageConverter)
	{
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter);
		return rabbitTemplate;
	}
}
