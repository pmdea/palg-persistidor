package persister.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import persister.core.domain.ObjectField;

@Repository
public interface ObjectFieldRepository extends JpaRepository<ObjectField, Integer>{
	
}
