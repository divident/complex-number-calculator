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
	
	public static ComplexNumber convertPolar(double z, double degree1, double degree2) {
		return new ComplexNumber(z * Math.cos(degree1), z * Math.sin(degree2));
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
	
	@Description(description="Add two complex number. Operator \" + \"")
	public ComplexNumber add(ComplexNumber c) {
		return new ComplexNumber(this.a + c.a, this.b + c.b);
	}
	
	@Description(description="Substitute two complex number. Operator \" - \"")
	public ComplexNumber sub(ComplexNumber c) {
		return new ComplexNumber(this.a - c.a, this.b - c.b);
	}
	
	@Description(description="Multiple two complex number. Operator \" * \"")
	public ComplexNumber mul(ComplexNumber c) {
		return new ComplexNumber(this.a * c.a - this.b * c.b, this.a * c.b + this.b * c.a);
	}

	@Description(description="Divide two complex number. Operator \" / \"")
	public ComplexNumber div(ComplexNumber c) {
		ComplexNumber tmp = this.mul(c.con());
		double d = c.a * c.a + c.b * c.b;
		return new ComplexNumber(tmp.a / d, tmp.b / d);
	}

	public double arg() {
		return Math.atan2(b, a);
	}

	public ComplexNumber con() {
		return new ComplexNumber(this.a, -1 * this.b);
	}

	public double mod() {
		return Math.sqrt(this.a * this.a + this.b * this.b);
	}
	
	@Description(description="Return square of complex number. Operator \" sqrt \"")
	public ComplexNumber sqrt() {
		double r = Math.sqrt(this.mod());
		double theta = this.arg() / 2;
		return new ComplexNumber(r * Math.cos(theta), r * Math.sin(theta));
	}
	
	@Description(description="Return imagine part of complex number. Operator \" im|IM \"")
	public ComplexNumber imPart() {
		this.setA(0);
		return this;
	}
	
	@Description(description="Return real part of complex number. Operator \" rm|RM \"")
	public ComplexNumber rePart() {
		this.setB(0);
		return this;
	}

	@Override
	public String toString() {
		return "ComplexNumber [Re=" + a + ", Im=" + b + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(a);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(b);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComplexNumber other = (ComplexNumber) obj;
		if (Double.doubleToLongBits(a) != Double.doubleToLongBits(other.a))
			return false;
		if (Double.doubleToLongBits(b) != Double.doubleToLongBits(other.b))
			return false;
		return true;
	}

}
