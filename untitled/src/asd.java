//import java.io.IOException;
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
import java.util.Scanner;

//public class asd {
//    public static void main(String[] args) throws IOException {
//        InputStream in = System.in;
//
//        byte[] a = new byte[3];
//        in.read(a);
//
//        System.out.println(a[0]);
//        System.out.println(a[1]);
//        System.out.println(a[2]);
//    }
//}

//public class asd {
//    public static void main(String[] args) throws IOException {
//        InputStream in = System.in;
//        InputStreamReader reader = new InputStreamReader(in);
//        char[] a = new char[3];
//        reader.read(a);
//
//        System.out.println(a);
//    }
//}

//public class asd {
//    public static void main(String[] args) throws IOException {
//        InputStream in = System.in;
//        InputStreamReader reader = new InputStreamReader(in);
//        BufferedReader br = new BufferedReader(reader);
//
//        String a = br.readLine();
//        System.out.println(a);
//    }
//}

//public class asd {
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        System.out.println(sc.next());
//    }
//}

class FoolException extends Exception {
}

public class asd {
    public void sayNick(String nick) throws FoolException {
        if("바보".equals(nick)) {
            throw new FoolException();
        }
        System.out.println("당신의 별명은 "+nick+" 입니다.");
    }

    public static void main(String[] args) {
        asd sample = new asd();
        try {
            sample.sayNick("바보");
            sample.sayNick("야호");
        } catch (FoolException e) {
            System.err.println("FoolException이 발생했습니다.");
        }
    }
}