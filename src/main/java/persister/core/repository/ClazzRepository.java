package persister.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import persister.core.domain.Clazz;

@Repository
public interface ClazzRepository extends JpaRepository<Clazz, Integer>{
	public Clazz findByName(String name);
}
