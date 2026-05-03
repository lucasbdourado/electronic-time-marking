package br.com.lucasbdourado.electronictimemarking.consumer;

import br.com.lucasbdourado.electronictimemarking.service.DailyRegisterService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.lucasbdourado.electronictimemarking.configuration.RabbitMQConstants.MARKING_REGISTER_QUEUE;

@Component
public class MarkingRegisteredConsumer
{
    private final DailyRegisterService dailyRegisterService;

    public MarkingRegisteredConsumer(DailyRegisterService dailyRegisterService) {
        this.dailyRegisterService = dailyRegisterService;
    }

    @RabbitListener(queues = MARKING_REGISTER_QUEUE)
    public void consume(TimeMarkRegisteredEvent event)
    {
        dailyRegisterService.create(event);
    }
}
