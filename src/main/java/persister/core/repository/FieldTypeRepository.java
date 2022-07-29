package persister.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import persister.core.domain.FieldType;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface FieldTypeRepository extends JpaRepository<FieldType, Integer>{
	public Optional<FieldType> findByName(String name);
}
