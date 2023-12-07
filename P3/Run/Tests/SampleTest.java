class SampleTest {
    public static void main(String[] args) {
        System.out.println(new mainClass().method1());
    }
}

class A {
   

    public int shuffle() {
       System.out.println(1);
        return 67;
    }
}

class B extends A {
    public int shuffle() {
       System.out.println(2);

        return 67;
    }

}

class C extends A {

}

class D extends B {
//     public int shuffle() {
//         System.out.println(3);
//        return 67;
//    }
}

class E extends D {
    public int shuffle() {
         System.out.println(4);
        return 67;
    }
}

class mainClass {
    A a;
    A b;
    A c;
    A d;
    A e;
    int count;

    public int method1() {
        boolean res;
        count = 0;
        a = new A();
        b = new B();
        c = new C();
        d = new D();
        e = new E();
        return this.shuffle(a, b, c, d, e);
    }

    public int shuffle(A a, A b, A c, A d, A e) {
        int out;
        out = a.shuffle();
        out = b.shuffle();
        out = c.shuffle();
        out = d.shuffle();
        out = e.shuffle();
        
        return out;
    }
}