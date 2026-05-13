//class HouseLee {
//    static String lastname = "이";
//}
//
//public class Sample {
//    public static void main(String[] args) {
//        HouseLee lee1 = new HouseLee();
//        HouseLee lee2 = new HouseLee();
//        System.out.println(lee1.lastname);
//        System.out.println(lee2.lastname);
//    }
//}

class Counter  {
    static int count = 0;
    Counter() {
        count++;
        System.out.println(count);
    }

    public static int getCount() {
        return count;
    }
}

public class Sample {
    public static void main(String[] args) {
        Counter c1 = new Counter();
        Counter c2 = new Counter();

        System.out.println(Counter.getCount());
    }
}