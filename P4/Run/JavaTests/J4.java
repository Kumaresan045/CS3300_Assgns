class J4{
    public static void main(String[] a){
	System.out.println(new Auxillary().func(0,1,2,3,4,5,6,7,8,9,10));
    }
}

class Auxillary
{
    public int func(int x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8, int x9,int x10, int x11)
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