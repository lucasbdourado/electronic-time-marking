package br.com.lucasbdourado.electronictimemarking.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.lucasbdourado.electronictimemarking.configuration.RabbitMQConstants.MARKING_REGISTER_QUEUE;

@Component
public class MarkingRegisteredConsumer
{
    @RabbitListener(queues = MARKING_REGISTER_QUEUE)
    public void consume(TimeMarkRegisteredEvent event)
    {
        System.out.println("Mensagem recebida do RabbitMQ:");
        System.out.println("Autor: " + event.author());
        System.out.println("Canal: " + event.channelId());
        System.out.println("Tipo: " + event.type());
        System.out.println("Horário: " + event.registeredAt());
    }
}
