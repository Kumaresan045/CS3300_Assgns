class T4{
    public static void main(String[] a)
    {
	    System.out.println((new TV().init()).stop());
    }
}

class TV
{
    int cnter;
    public TV _foo()
    {
        cnter = cnter + 1;
        System.out.println(cnter);
        return this;
    }
    public TV init()
    {
        cnter = 0;
        return this;
    }
    public int stop()
    {
        return 0;
    }
}