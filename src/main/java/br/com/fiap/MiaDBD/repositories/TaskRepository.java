package br.com.fiap.MiaDBD.repositories;

import br.com.fiap.MiaDBD.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    Optional<Task> findById(Integer id);

}
