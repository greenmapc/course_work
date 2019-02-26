package ru.itis.teamwork.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itis.teamwork.models.ProjectFile;

@Repository
public interface ProjectFileRepository extends CrudRepository<ProjectFile, Long> {
}
