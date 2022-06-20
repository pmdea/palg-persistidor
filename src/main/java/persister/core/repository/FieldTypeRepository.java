package persister.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import persister.core.domain.FieldType;

public interface FieldTypeRepository extends JpaRepository<FieldType, Integer>{
	public FieldType findByName(String name);
}
