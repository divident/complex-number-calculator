import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class MethodReader {
	private static String className = "ComplexNumber";
	public static Map<String, String> methodDescription = new HashMap<>();
	
	public static Map<String, String> getMethods() {
		try {
			Class<?> c = Class.forName(className);

			Method[] methods = c.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				if (!methods[i].isAnnotationPresent(Description.class))
					continue;
				Description description = (Description) methods[i].getAnnotation(Description.class);
				//System.out.println("Method description: " + description.description());
				methodDescription.put(methods[i].getName(), description.description());
			}

		} catch (ClassNotFoundException exp) {
			exp.printStackTrace();
		}
		return methodDescription;
	}
}
