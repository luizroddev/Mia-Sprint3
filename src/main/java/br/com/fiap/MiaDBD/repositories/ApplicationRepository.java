package br.com.fiap.MiaDBD.repositories;

import br.com.fiap.MiaDBD.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    Optional<Application> findById(Integer id);
    Optional<Application> findByName(String name);


}
