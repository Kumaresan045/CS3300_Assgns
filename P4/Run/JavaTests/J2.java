class J2{
    public static void main(String[] a){
	System.out.println(new Auxillary().func1(10,20));
    }
}

class Auxillary
{
    public int func1(int a, int b)
    {
        int c;
        c = a + b;
        return c;
    }
}