package br.com.lucasbdourado.electronictimemarking.infrastructure.messaging.consumer;

import br.com.lucasbdourado.electronictimemarking.domain.entity.Author;
import br.com.lucasbdourado.electronictimemarking.domain.entity.RegisterType;

import java.time.LocalDateTime;

public record TimeMarkRegisteredEvent(Author author, RegisterType type, LocalDateTime registeredAt)
{
}
