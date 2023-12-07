class Main 
{
	public static void main (String [] a)
	{
		System.out.println(1);
	}
}

class A
{
	int x;
	boolean y;

	public int huh()
	{
		x=y;
		return 0;
	}
}

class Test extends A{
	public int ff(int x)
	{
		return 70;
	}
}