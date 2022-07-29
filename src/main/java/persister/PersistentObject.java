package persister;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import persister.core.domain.Clazz;
import persister.core.domain.ObjectField;
import persister.core.domain.PersistableObject;
import persister.core.domain.Session;
import persister.core.repository.*;
import persister.exception.StructureChangedException;

@Service
public class PersistentObject implements IPersistentObject{

	private SessionRepository sessionRepo;
	private ClazzRepository clazzRepo;
	private PersistableObjectRepository persistableObjectRepo;
	private FieldTypeRepository fieldTypeRepo;
	private ObjectFieldRepository objectFieldRepo;

	public PersistentObject(SessionRepository sessionRepo, ClazzRepository clazzRepo, PersistableObjectRepository persistableObjectRepo, FieldTypeRepository fieldTypeRepo, ObjectFieldRepository objectFieldRepo) {
		this.sessionRepo = sessionRepo;
		this.clazzRepo = clazzRepo;
		this.persistableObjectRepo = persistableObjectRepo;
		this.fieldTypeRepo = fieldTypeRepo;
		this.objectFieldRepo = objectFieldRepo;
	}

	private Predicate<PersistableObject> isOfClass(String clazzName){
		return obj -> obj.getClazzId().getName().equalsIgnoreCase(clazzName);
	}
	
	@Override
	public boolean store(long sId, Object o) {
		Session currentSession = getCurrentSession(sId);
		Clazz currentClazz = null;
		try {
			currentClazz = getCurrentClazz(o);
		} catch (StructureChangedException e) {
			e.printStackTrace();
			return false;
		}
		PersistableObject currentPersistableObject = getCurrentPersistableObject(currentClazz, currentSession);
		PersistableObject updatedPersistableObject = new ObjectParser(objectFieldRepo).toPersistable(currentPersistableObject, o, currentClazz, currentSession);
		persistableObjectRepo.save(updatedPersistableObject);
		return true;
	}

	private PersistableObject getCurrentPersistableObject(Clazz clazz, Session session) {
		Optional<PersistableObject> storedObject = Optional.empty(); //TODO: persistableObjectRepo.findByClassAndSession(clazzId, sessionId)
		if (storedObject.isPresent()) {
			return storedObject.get();
		}
		PersistableObject obj = new PersistableObject();
		obj.setClazzId(clazz);
		obj.setSessionId(session);
		return obj;
	}

	private Session getCurrentSession(long sId) {
		Optional<Session> storedSession = sessionRepo.findById(sId);
		if (storedSession.isPresent()) {
			Session s = storedSession.get();
			Date date = new Date();
			s.setLast_access(date.getTime());
			return sessionRepo.save(s);
		}
		Session newSession = new Session();
		Date date = new Date();
		newSession.setLast_access(date.getTime());
		newSession.setId(sId);
		return sessionRepo.save(newSession);
	}

	private Clazz getCurrentClazz(Object obj) throws StructureChangedException {
		Clazz clazz = ClazzParser.getInstance(fieldTypeRepo).toClazzFromObject(obj);
		int exists = CompareClass.existsInDB(clazzRepo, clazz);
		if (exists == 0) {
			return clazzRepo.save(clazz);
		} else if (exists == 1) {
			return clazzRepo.findByName(clazz.getName());
		}else {
			Clazz existingClazz = clazzRepo.findByName(ClazzParser.getInstance().getName(clazz));
			//TODO: persistableObjectRepo.deleteAllByClassId(existingClazz.getId());
			//remover la clase
			//TODO: clazzRepo.deleteByName(existingClazz.getName());
			//guardar la clase nueva
			return clazzRepo.save(clazz);
		}

	}

	@Override
	public <T> T load(long sId, Class<T> clazz) throws StructureChangedException {
		// TODO Auto-generated method stub
		/*
			0. Obtengo nombre de clase
			1. Busco clase por nombre, existe?
				1-1-a. Obtengo Clazz de clazz y comparo para ver si son iguales, si son distintas tiro error StrutureChangedException
			else
				1-2-a. No se puede hacer load porque no existe clase, por ende tampoco objeto <-- Ver que hacer
			2. Existe objeto con esa clase y esa sesion?
				2-1-a. Parseo de PersistentObject al tipo dado
				2-1-b. Actualizo timestamp
				2-1-c. Retorno objeto
			else
				2-2-a. Objeto no encontrado
		 */
		String className = clazz.getName();
		Optional<Session> sessionOpt = sessionRepo.findById(sId);
		if(sessionOpt.isPresent()) {
			//Obtengo la sesion
			Session session = sessionOpt.get();
			List<PersistableObject> objectsFromSession = findObjectByClassName(session.getPersistableObject(), className); //Verifico que tenga un objeto con la class
			if(objectsFromSession.isEmpty())
				return null; //no tiene objeto, return null
			PersistableObject obj = objectsFromSession.get(0);
			if(hasStructureChanged(clazz, obj.getClazzId()))
				throw new StructureChangedException("Structure of class " + clazz.getName() + "has changed.");
			T object = rebuildObject(clazz, obj);
			session.setLast_access(Instant.now().getEpochSecond());
			return object;
		}
		
		
		return null;
	}
	
	private <T> T rebuildObject(Class<T> c, PersistableObject obj) {
		T recon = null;
		try {
			recon = c.newInstance();
			for(Field f : c.getDeclaredFields()) {
				f.setAccessible(true);
				ObjectField objField = obj.getObjectFields().stream().filter(field -> field.getFieldId().getName().equals(f.getName())).collect(Collectors.toList()).get(0);
				
			}
			
		} catch (InstantiationException e) {} catch (IllegalAccessException e) {}
		
		return recon;
	}
	
	private boolean hasStructureChanged(Class classJ, Clazz classDb) {
		
		return false;
	}
	
	private boolean isPrimitive(Object obj) {
		return obj.getClass().isPrimitive()
				|| obj instanceof String
                || obj instanceof Integer
                || obj instanceof Double
                || obj instanceof Boolean;
	}

	@Override
	public <T> boolean exists(long sId, Class<T> clazz) {
		
		Optional<Session> session = sessionRepo.findById(sId);
		if(session.isPresent()) {
			Session actual = session.get();
			actual.setLast_access(Instant.now().getEpochSecond());
			return !findObjectByClassName(actual
					.getPersistableObject(), clazz.getName())
					.isEmpty();
		}
		
		return false;
	}
	
	private List<PersistableObject> findObjectByClassName(List<PersistableObject> persistableList, String className){
		return persistableList.stream()
				.filter(isOfClass(className))
				.collect(Collectors.toList());
	}

	@Override
	public long elapsedTime(long sId) {
		Optional<Session> session = sessionRepo.findById(sId);
		if(session.isPresent())
			return Instant.now().getEpochSecond() - session.get().getLast_access();
		return -1; //Si no existe la sesion
	}

	@Override
	public <T> T delete(long sId, Class<T> clazz) {
		/*
			Llamar a load y catchear errores
			Si hay errores de clase corrupta o no existe objeto, que hacer?
			Si no hay error, buscar clase por nombre para obtener ID y eliminar el Persisted Object segun sId y classId
			Se puede hacer una funcion auxiliar que devuelva el objeto ya parseado y el classId, para reutilizar logica entre delete y store.
			Tambien puede tener un booleano si busca eliminar o leer
		 */
		T deletedObject = null;
		try {
			deletedObject = load(sId, clazz);
			if(sessionRepo.findById(sId).isPresent()) {
				Session session = sessionRepo.findById(sId).get();
				session.getPersistableObject().removeIf(isOfClass(clazz.getName()));
				session.setLast_access(Instant.now().getEpochSecond());
				sessionRepo.save(session);
			}
			return deletedObject;
			 
		} catch (StructureChangedException e) { }
		return deletedObject;
	}

}
