package persister.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import persister.core.domain.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>{

}
