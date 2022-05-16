package persistidor;

import persistidor.exception.StructureChangedException;

public class PersistentObject implements IPersistentObject{

	@Override
	public boolean store(long sId, Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T load(long sId, Class<T> clazz) throws StructureChangedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> boolean exists(long sId, Class<T> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long elapsedTime(long sId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> T delete(long sId, Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
