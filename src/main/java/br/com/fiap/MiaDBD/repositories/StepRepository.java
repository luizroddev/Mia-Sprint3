package br.com.fiap.MiaDBD.repositories;

import br.com.fiap.MiaDBD.models.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepRepository extends JpaRepository<Step, Integer> {
    // Você pode adicionar consultas personalizadas aqui, se necessário.
}
