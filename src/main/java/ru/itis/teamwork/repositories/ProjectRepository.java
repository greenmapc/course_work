package ru.itis.teamwork.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itis.teamwork.models.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
}
