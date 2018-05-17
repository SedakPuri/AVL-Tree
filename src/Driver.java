import java.util.Scanner;

public class Driver {

	public static void main(String[] args)
	{
		AVL<Integer> tree = new AVL<>();
		
		for(int i=0; i < 15; i++)
		{
			tree.add(i);
		}
		
		System.out.println(tree);
		
		Scanner input = new Scanner(System.in);
		
		while(true)
		{
			System.out.println("Value to remove");
			int v = input.nextInt();
			tree.remove(v);
			System.out.println(tree);
		}
		
		
		
	}
}
