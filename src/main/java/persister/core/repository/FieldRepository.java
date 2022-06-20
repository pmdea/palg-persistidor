package persister.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import persister.core.domain.Field;

@Repository
public interface FieldRepository extends JpaRepository<Field, Integer> {
	public Field findByName(String name);
}
