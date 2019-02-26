package ru.itis.teamwork.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itis.teamwork.models.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
