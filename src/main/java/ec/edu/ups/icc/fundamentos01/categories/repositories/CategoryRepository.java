package ec.edu.ups.icc.fundamentos01.categories.repositories;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}