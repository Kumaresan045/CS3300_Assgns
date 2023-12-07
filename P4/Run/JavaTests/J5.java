class J5{
    public static void main(String[] a){
	System.out.println(new Auxillary().start());
    }
}

class Auxillary
{
	int x;
    public int start()
    {
		int dump;
        x=4;
		System.out.println(x);
		dump = this.side_effect();
		dump = 10;
		System.out.println(x);
		System.out.println(dump);
		return 0;
    }

	public int side_effect()
	{
		x=5;
		System.out.println(x);
		return 7;
	}
}