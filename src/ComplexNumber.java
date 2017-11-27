public class ComplexNumber {
	private double a, b;

	public ComplexNumber(double a, double b) {
		super();
		this.a = a;
		this.b = b;
	}

	public static ComplexNumber convertPolar(double z, double degree) {
		return new ComplexNumber(z * Math.cos(degree), z * Math.sin(degree));
	}

	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}

	public void setA(double a) {
		this.a = a;
	}

	public void setB(double b) {
		this.b = b;
	}

	public ComplexNumber add(ComplexNumber c) {
		return new ComplexNumber(this.a + c.a, this.b + c.b);
	}

	public ComplexNumber sub(ComplexNumber c) {
		return new ComplexNumber(this.a - c.a, this.b - c.b);
	}

	public ComplexNumber mul(ComplexNumber c) {
		return new ComplexNumber(this.a * c.a - this.b * c.b, this.a * c.b + this.b * c.a);
	}

	public ComplexNumber div(ComplexNumber c) {
		ComplexNumber tmp = this.mul(c.con());
		double d = c.a * c.a + c.b * c.b;
		return new ComplexNumber(tmp.a / d, tmp.b / d);
	}

	public ComplexNumber con() {
		return new ComplexNumber(this.a, -1 * this.b);
	}

	public double mod() {
		return Math.sqrt(this.a * this.a + this.b * this.b);
	}
}
