class J3{
    public static void main(String[] a){
	System.out.println(new Auxillary().func());
    }
}

class Auxillary
{
    public int func()
    {
        int a;
		a = this.fun2(2,3,4,5,6,7,8);
		return 0;
    }

	public int fun2(int a, int b, int c, int e, int f, int g, int h)
	{
		b=5;
		h=4;
		g=8;
		f=9;
		System.out.println(a);
		System.out.println(b);
		return 0;
	}
}