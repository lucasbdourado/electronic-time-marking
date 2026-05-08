package br.com.lucasbdourado.electronictimemarking.domain.repository;

import br.com.lucasbdourado.electronictimemarking.domain.entity.TimeMark;

public interface TimeMarkRepository
{
	TimeMark save(TimeMark timeMark);
}
