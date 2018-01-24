public final class CompilationClass {

    public static void main(String[] args) {
        System.out.println((new ComplexNumber(Double.parseDouble(args[0]), Double.parseDouble(args[1])))
        		.add(new ComplexNumber(Double.parseDouble(args[2]), Double.parseDouble(args[3])))
        		.add(new ComplexNumber(Double.parseDouble(args[4]), Double.parseDouble(args[5])))
        		.add(new ComplexNumber(Double.parseDouble(args[6]), Double.parseDouble(args[7]))));
    }
}
