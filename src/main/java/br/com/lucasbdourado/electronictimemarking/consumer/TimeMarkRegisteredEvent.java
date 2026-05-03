package br.com.lucasbdourado.electronictimemarking.consumer;

import br.com.lucasbdourado.electronictimemarking.domain.Author;
import br.com.lucasbdourado.electronictimemarking.domain.RegisterType;

import java.time.LocalDateTime;

public record TimeMarkRegisteredEvent(Author author, String channelId, RegisterType type,
                                      LocalDateTime registeredAt)
{
}
