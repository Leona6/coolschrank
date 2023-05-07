package de.markus.meier.coolschrank.repository;

import de.markus.meier.coolschrank.model.model.ShoppingListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * The ShoppingListRepository interface is responsible for accessing ShoppingListEntity in the database.
 */
public interface ShoppingListRepository extends JpaRepository<ShoppingListEntity, Long> {
}
