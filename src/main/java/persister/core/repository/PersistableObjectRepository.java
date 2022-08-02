package persister.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import persister.core.domain.PersistableObject;

@Repository
public interface PersistableObjectRepository extends JpaRepository<PersistableObject, Integer>{
	
	List<PersistableObject> findByClazzId(int id);
	Optional<PersistableObject> findByClazzId_IdAndSessionId_Id(int clazzId, long sessionId);
	
}
