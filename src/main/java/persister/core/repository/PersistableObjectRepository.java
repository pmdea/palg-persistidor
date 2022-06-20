package persister.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import persister.core.domain.PersistableObject;

@Repository
public interface PersistableObjectRepository extends JpaRepository<PersistableObject, Integer>{

}
