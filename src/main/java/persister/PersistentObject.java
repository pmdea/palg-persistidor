package persister;

import persister.exception.StructureChangedException;

public class PersistentObject implements IPersistentObject{

	@Override
	public boolean store(long sId, Object o) {
		// TODO Auto-generated method stub
		/*
		* 1. Existe sesion?
		* 	1-1-a. Actualizo timestamp
		* else
		* 	1-2-a. La creo con nuevo timestamp
		* 2. Obtener nombre de la clase de "o"
		* 3. Obtener ID de la clase
		* 4. Es una clase nueva? (no existe ID. Si no existe la clase, entonces el objeto tampoco)
		* 	4-1-a. Obtener Clazz de "o"
		* 	4-1-b. Storear la clase y obtener ID
		* 5. Obtener PersistentObject de "o"
		* 6. Obtener objeto para esa clase y esa sesion
		* 7. Existe objeto?
		* 	7-1-a. Hacer update de ese objectId usando PersistentObject obtenido en 4
		* 	7-1-b. Devolver true
		* else
		* 	7-2-a. Crear nuevo PersistentObject obtenido en 4
		* 	7-2-b. Devolver false
		*/
		return false;
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
		return null;
	}

	@Override
	public <T> boolean exists(long sId, Class<T> clazz) {
		// TODO Auto-generated method stub
		/*
			1. Obtengo nombre de clase
			2. No existe clase?
				2.1.a. Retorno false
			3. Busco objeto por classId y sessionId, retorno existencia
		 */
		return false;
	}

	@Override
	public long elapsedTime(long sId) {
		// TODO Auto-generated method stub
		/*
			1. Busco session
			2. Existe?
				Retorno elapsedTime
			else
				Error? Retorna 0? <-- Ver que hacer
		 */
		return 0;
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
		return null;
	}

}
