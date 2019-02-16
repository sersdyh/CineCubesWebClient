package exctractionmethod;

public class ExtractionMethodFactory {
	
	public static ExtractionMethod createMethod(){
		return new SqlQuery();

	}
}
