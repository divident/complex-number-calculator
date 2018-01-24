

public class ClassMethod {
	public ClassMethod(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	@Override
	public String toString() {
		return name;
	}

	public String name;
	public String description;
}
