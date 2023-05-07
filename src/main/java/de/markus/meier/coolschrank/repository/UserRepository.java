package de.markus.meier.coolschrank.repository;

import de.markus.meier.coolschrank.model.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * The UserRepository interface is responsible for accessing user data in the database.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
