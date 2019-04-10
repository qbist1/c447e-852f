package nz.co.cocca.test.javatest;

public class debug {

	public static void main(String[] args) 
	{
		usersdoc element = new usersdoc();
		if (!element.execute()) 
		{
			System.out.print("ERROR"  + element.err);
			System.exit(0);
			return;
		}
		if (!element.sort("first_name"))
		{
			System.out.print("ERROR"  + element.err);
			System.exit(0);
			return;
		}
		System.out.print(element.as_string());
		System.exit(0);
		return;
	}

}
