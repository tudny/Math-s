package math.app.classes;

public class Pair<Type1, Type2> {

	private Type1 first;
	private Type2 second;


	public Type1 getFirst() {
		return first;
	}

	public void setFirst(Type1 first) {
		this.first = first;
	}


	public Type2 getSecond() {
		return second;
	}

	public void setSecond(Type2 second) {
		this.second = second;
	}


	public Pair(Type1 first, Type2 second){
		this.first = first;
		this.second = second;
	}

	public static Pair<Object, Object> make_pair(Object first, Object second){
		return new Pair<>(first, second);
	}

	public Pair(){
		try {
			first = null;
			second = null;
		} catch (Exception e){
			System.out.println("New empty pair error: " + e.getMessage());
		}
	}

	@Override
	public int hashCode() {
		return first.hashCode() * 313 + second.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return "[" + first + ", " + second + "]";
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
}
