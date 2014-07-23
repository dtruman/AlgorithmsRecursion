import java.util.List;


public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExhaustiveDecoder ed=new ExhaustiveDecoder();
		
		List<String> al=ed.decode("....-.-.-.---..--.-..--..-...--..-.........-.....-.-.----.........-----.-.-.-...-.--..--..-...-..-..----...--.-...--..-.-.--.-.----.");
		
		for(String var : al)
		{
			System.out.println(var);
			System.out.println();
		}
	}

}
