package br.com.lucasbdourado.electronictimemarking.infrastructure.messaging.consumer;

import br.com.lucasbdourado.electronictimemarking.application.service.DailyRegisterApplicationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.lucasbdourado.electronictimemarking.infrastructure.configuration.RabbitMQConstants.MARKING_REGISTER_QUEUE;

@Component
public class MarkingRegisteredConsumer
{
	private final DailyRegisterApplicationService dailyRegisterApplicationService;

	public MarkingRegisteredConsumer(DailyRegisterApplicationService dailyRegisterApplicationService)
	{
		this.dailyRegisterApplicationService = dailyRegisterApplicationService;
	}

	@RabbitListener(queues = MARKING_REGISTER_QUEUE)
	public void consume(TimeMarkRegisteredEvent event)
	{
		dailyRegisterApplicationService.create(event);
	}
}
