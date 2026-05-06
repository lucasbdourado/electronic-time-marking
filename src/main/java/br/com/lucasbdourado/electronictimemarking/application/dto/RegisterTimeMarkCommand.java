package br.com.lucasbdourado.electronictimemarking.application.dto;

import br.com.lucasbdourado.electronictimemarking.domain.entity.Author;
import br.com.lucasbdourado.electronictimemarking.domain.entity.RegisterType;
import java.time.LocalDateTime;

public record RegisterTimeMarkCommand(Author author, RegisterType type, LocalDateTime registeredAt)
{
}
