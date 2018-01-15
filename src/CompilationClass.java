public final class CompilationClass {

    public static void main(String[] args) {
        System.out.println("Compiled result = " + execute());
    }

    private static ComplexNumber execute() {
        return new ComplexNumber(3.2, 2.1);
    }
}
