package br.com.lucasbdourado.electronictimemarking.infrastructure.messaging.consumer;

import br.com.lucasbdourado.electronictimemarking.application.dto.RegisterTimeMarkCommand;
import br.com.lucasbdourado.electronictimemarking.application.dto.WorkDayResponse;
import br.com.lucasbdourado.electronictimemarking.application.service.DailyRegisterApplicationService;
import org.springframework.amqp.core.Message;
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
	public WorkDayResponse consume(TimeMarkRegisteredEvent event, Message message)
	{
		RegisterTimeMarkCommand command = new RegisterTimeMarkCommand(event.authorDiscordId(),
			event.authorCode(), event.authorName(), event.type(), event.registeredAt());

		WorkDayResponse response = dailyRegisterApplicationService.create(command);

		if (message.getMessageProperties().getReplyTo() == null)
		{
			return null;
		}

		return response;
	}
}
