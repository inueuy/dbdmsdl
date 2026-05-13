import java.util.Scanner;
import java.util.ArrayList;

class Student {
    static int nextId = 260101;

    String name;
    int id;
    String gender;
    double[] grade; // 학점: 4개의 점수 저장 필요
    int age;
    String contact;

    public Student(String inputName){
        this.name = inputName;
        this.id = nextId++;
        this.gender = "미입력";
        this.grade = new double[4];
        this.age = 0;
        this.contact = "미입력";
    }

    void setGender(String inputGender){
        this.gender = inputGender;
    }
    void setAge(int inputAge){
        this.age = inputAge;
    }
    void setContact(String inputContact){
        this.contact = inputContact;
    }
    void setGrade(int inputSemester, double inputGrade){
        if(inputSemester > 0 && inputSemester <= 4){
            this.grade[inputSemester-1] = inputGrade;
        }
        else{
            System.out.println("잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    public int getId(){
        return this.id;
    }

    public void showInfo(){
        System.out.println("학번: " + this.id);
        System.out.println("이름: " + this.name);
        System.out.println("성별: " + this.gender);
        System.out.println("연락처: " + this.contact);
        System.out.println("나이: " + this.age);
        System.out.println("학기별 성적: ");
        for (int i = 0; i < 4; i++){
            System.out.println((i+1) + "학기 [" + grade[i] + "]");
        }
        System.out.println("----------------------");
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ArrayList<Student> studentsList = new ArrayList<>();


        while(true){
            System.out.println("===== 학적 관리 시스템 =====");
            System.out.println("1. 학생 등록 | 2. 정보 수정 | 3. 전체 명단 출력 | 4. 종료");
            System.out.print("메뉴 선택 -> ");
            String menu = sc.nextLine();
            if (menu.equals("1")){
                System.out.print("등록할 학생의 이름 : ");
                String name = sc.nextLine();
                Student newStudent = new Student(name);
                studentsList.add(newStudent);
                System.out.println("등록 완료. 학번은 " + newStudent.getId() + "입니다.");


            } else if(menu.equals("2")){
                while(true){
                    System.out.print("수정할 학생의 학번 -> ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    int collectState = 0;
                    for(Student s : studentsList){
                        if(id == s.getId()){
                            collectState = 1;
                            System.out.println("1. 성별 | 2. 연락처 | 3. 나이 | 4. 성적 | 0. 처음으로 돌아가기");
                            System.out.print("메뉴 선택 -> ");

                            String choice = sc.nextLine();
                            if(choice.equals("1")){
                                System.out.print("성별에 해당하는 번호 입력. 남자(1) 여자(2) -> ");
                                choice = sc.nextLine();
                                if(choice.equals("1")){
                                    s.setGender("남");
                                    break;
                                } else if(choice.equals("2")){
                                    s.setGender("여");
                                    break;
                                } else {
                                    System.out.println("잘못된 입력. 다시 입력해 주세요.");
                                    break;
                                }
                            }else if(choice.equals("2")){
                                System.out.print("연락처 입력 -> ");
                                String contact = sc.nextLine();
                                s.setContact(contact);
                                break;
                            }else if(choice.equals("3")){
                                System.out.print("나이 입력 -> ");
                                String ageInput = sc.nextLine();
                                int age = Integer.parseInt(ageInput);
                                s.setAge(age);
                                break;
                            }else if(choice.equals("4")){
                                System.out.print("학기 입력 (1~4) -> ");
                                int semester = Integer.parseInt(sc.nextLine());
                                System.out.print("성적 입력 -> ");
                                double grade = Double.parseDouble(sc.nextLine());

                                s.setGrade(semester, grade);
                                break;
                            }else{
                                collectState = -1;
                                break;
                            }
                        }
                    }
                    if(collectState == 0){
                        System.out.println("일치하는 학생이 없음.");
                    } else if(collectState == -1){
                        System.out.println("처음으로 돌아갑니다.");
                        break;
                    }
                }




            } else if(menu.equals("3")){
                for(Student s : studentsList){
                    s.showInfo();
                }
            }else if (menu.equals("4")){
                System.out.println("프로그램 종료");
                break;
            }else{
                System.out.println("잘못된 입력. 다시 입력해 주세요.");
            }
        }

    }
}