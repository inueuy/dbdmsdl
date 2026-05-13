import java.util.Scanner;
import java.util.ArrayList;

class Student {
    static int nextId = 260101;

    private String name;
    private int id;
    private String gender;
    private double[] grade;
    private int age;
    private String contact;

    public Student(String inputName) {
        this.name = inputName;
        this.id = nextId++;
        this.gender = "미입력";
        this.grade = new double[4];
        this.age = 0;
        this.contact = "미입력";
    }

    void setGender(String inputGender) { this.gender = inputGender; }
    void setAge(int inputAge) { this.age = inputAge; }
    void setContact(String inputContact) { this.contact = inputContact; }
    void setGrade(int inputSemester, double inputGrade) {
        if (inputSemester > 0 && inputSemester <= 4) {
            this.grade[inputSemester - 1] = inputGrade;
        } else {
            System.out.println("잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    public int getId() { return this.id; }
    public String getName() { return this.name; }

    public void showInfo() {
        System.out.println("학번: " + this.id);
        System.out.println("이름: " + this.name);
        System.out.println("성별: " + this.gender);
        System.out.println("연락처: " + this.contact);
        System.out.println("나이: " + this.age);
        System.out.println("학기별 성적: ");
        for (int i = 0; i < 4; i++) {
            System.out.println((i + 1) + "학기 [" + grade[i] + "]");
        }
        System.out.println("----------------------");
    }
}

class GraduateStudent extends Student {
    private String labName;
    private String advisor;

    public GraduateStudent(String name, String labName, String advisor) {
        super(name);
        this.labName = labName;
        this.advisor = advisor;
    }

    public void setLabName(String labName) { this.labName = labName; }
    public void setAdvisor(String advisor) { this.advisor = advisor; }

    @Override
    public void showInfo() {
        super.showInfo();
        System.out.println("연구실 : " + labName);
        System.out.println("지도 교수 : " + advisor);
        System.out.println("구분 : 대학원생");
        System.out.println("----------------------");
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> studentsList = new ArrayList<>();

        while (true) {
            System.out.println("\n===== 학적 관리 시스템 =====");
            System.out.println("1. 학생 등록 | 2. 정보 수정 | 3. 전체 명단 출력 | 4. 종료");
            System.out.print("메뉴 선택 -> ");
            String menu = sc.nextLine();

            if (menu.equals("1")) {
                System.out.print("등록할 학생의 이름 : ");
                String name = sc.nextLine();
                System.out.print("대학생(1) 대학원생(2) -> ");
                String type = sc.nextLine();

                Student newStudent;
                if (type.equals("2")) {
                    System.out.print("연구실 이름 : ");
                    String lab = sc.nextLine();
                    System.out.print("지도 교수 이름 : ");
                    String advisor = sc.nextLine();
                    newStudent = new GraduateStudent(name, lab, advisor);
                } else {
                    newStudent = new Student(name);
                }
                studentsList.add(newStudent);
                System.out.println("등록 완료. 학번은 " + newStudent.getId() + "입니다.");

            } else if (menu.equals("2")) {
                while (true) {
                    System.out.print("수정할 학생의 학번 (0 입력 시 처음으로) -> ");
                    String inputId = sc.nextLine();
                    int id = Integer.parseInt(inputId);
                    if (id == 0) break;

                    Student target = null;
                    for (Student s : studentsList) {
                        if (id == s.getId()) {
                            target = s;
                            break;
                        }
                    }

                    if (target == null) {
                        System.out.println("일치하는 학생이 없습니다.");
                        continue;
                    }

                    System.out.println("\n[" + target.getId() + "번 " + target.getName() + " 학생 정보 수정]");
                    System.out.print("1. 성별 | 2. 연락처 | 3. 나이 | 4. 성적");
                    if (target instanceof GraduateStudent) {
                        System.out.print(" | 5. 연구실 | 6. 지도교수");
                    }
                    System.out.println(" | 0. 처음으로 돌아가기");
                    System.out.print("메뉴 선택 -> ");
                    String choice = sc.nextLine();

                    if (choice.equals("0")) break;
                    else if (choice.equals("1")) {
                        System.out.print("성별 입력 (남/여) -> ");
                        target.setGender(sc.nextLine());
                    } else if (choice.equals("2")) {
                        System.out.print("연락처 입력 -> ");
                        target.setContact(sc.nextLine());
                    } else if (choice.equals("3")) {
                        System.out.print("나이 입력 -> ");
                        target.setAge(Integer.parseInt(sc.nextLine()));
                    } else if (choice.equals("4")) {
                        System.out.print("학기 입력 (1~4) -> ");
                        int semester = Integer.parseInt(sc.nextLine());
                        System.out.print("성적 입력 -> ");
                        double grade = Double.parseDouble(sc.nextLine());
                        target.setGrade(semester, grade);
                    } else if (choice.equals("5") && target instanceof GraduateStudent) {
                        System.out.print("수정할 연구실 이름 -> ");
                        ((GraduateStudent) target).setLabName(sc.nextLine());
                    } else if (choice.equals("6") && target instanceof GraduateStudent) {
                        System.out.print("수정할 지도교수 이름 -> ");
                        ((GraduateStudent) target).setAdvisor(sc.nextLine());
                    } else {
                        System.out.println("잘못된 입력입니다.");
                        continue;
                    }
                    System.out.println("수정이 완료되었습니다.");
                    break;
                }
            } 

            else if (menu.equals("3")) {
                if (studentsList.isEmpty()) {
                    System.out.println("등록된 학생이 없습니다.");
                } else {
                    for (Student s : studentsList) {
                        s.showInfo();
                    }
                }
            } else if (menu.equals("4")) {
                System.out.println("프로그램 종료");
                break;
            } else {
                System.out.println("잘못된 입력. 다시 입력해 주세요.");
            }
        }
        sc.close();
    }
}