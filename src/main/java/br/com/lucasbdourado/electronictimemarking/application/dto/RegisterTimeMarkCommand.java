package br.com.lucasbdourado.electronictimemarking.application.dto;

import br.com.lucasbdourado.electronictimemarking.domain.entity.RegisterType;
import java.time.LocalDateTime;

public record RegisterTimeMarkCommand(Long authorDiscordId, String authorCode, String authorName,
                                      RegisterType type, LocalDateTime registeredAt)
{
}
