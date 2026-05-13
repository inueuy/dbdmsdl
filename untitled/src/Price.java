//class cu {
//    int cola = 2300;
//    int milk = 1500;
//}
//
//class gs25 {
//    int cola = 2100;
//    int milk = 1800;
//}
//
//public class Price {
//    public static void main(String[] args) {
//        cu a = new cu();
//        gs25 b = new gs25();
//
//        System.out.println(a.cola);
//        System.out.println(a.milk);
//        System.out.println(b.cola);
//        System.out.println(b.milk);
//    }
//}

//class cu {
//    int cola = 2300;
//
//    void order(int count) {
//        int total = cola * count;
//        System.out.println("cu 콜라 " + count + "개 가격: " + total + "원");
//    }
//}
//
//public class Price {
//    public static void main(String[] args) {
//        cu a = new cu();
//
//        a.order(3);
//    }
//}

//class gs25 {
//    int milk = 1800;
//
//    int getPrice(int count) {
//        int total = milk * count;
//        return total;
//    }
//}
//
//public class Price {
//    public static void main(String[] args) {
//        gs25 b = new gs25();
//
//        int result = b.getPrice(2);
//
//        System.out.println("결제 금액: " + result);
//    }
//}

class cu {
    int cola = 2300;

    int getPrice(int count) {
        return cola * count;
    }
}

public class Price {
    static void printResult(String name, int total) {
        System.out.println(name + "편의점 콜라 3개: " + total + "원");
    }

    public static void main(String[] args) {
        cu a = new cu();

        int result = a.getPrice(3);

        printResult("cu", result);
    }
}