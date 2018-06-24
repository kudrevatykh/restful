package restful.dao;

public class ResourceExistsException extends RuntimeException {
	
	private static final long serialVersionUID = -7447211156312327846L;

	public ResourceExistsException(String id) {
		super("resource "+id+ " already exists");
	}

}
