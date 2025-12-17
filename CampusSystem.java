package oel;
import java.util.*;

class Person {
    String name, id, entryTime;

    Person() {}
    Person(String n, String i, String t) 
    {
        name = n; id = i; entryTime = t;
    }
    boolean validateID() {
        if (id.equals("") || id.replace(id.charAt(0)+"","").equals("")) return false;
        for(char c:id.toCharArray()) if(!Character.isLetterOrDigit(c)) return false;
        return true;
    }
    void displayInfo() {
        System.out.println("Name: " + name);
        System.out.println("ID: " + id);
        System.out.println("Entry Time: " + entryTime);
    }
}
class Student extends Person {
    int age, semester, lateCount;
    String department;
    int marks[], attendance[] = new int[30];
    double cgpa;

    Student(String n, String i, int a, String d, int s) {
        super(n, i, "N/A");
        age=a; department=d; semester=s;
    }

    void computeCGPA() {
        int sum=0;
        for(int m:marks) sum+=m;
        cgpa = (sum/(double)marks.length)/10.0;
    }
    void displayInfo() {
        super.displayInfo();
        System.out.println("Age: " + age);
        System.out.println("Department: " + department);
        System.out.println("Semester: " + semester);
        System.out.println("CGPA: " + cgpa);
        System.out.println("Late Count: " + lateCount);
    }
}
class Staff extends Person {
    String department, shift;
    Staff(String n, String i, String t, String d, String s) {
        super(n,i,t); department=d; shift=s;
    }
    void displayInfo() {
        super.displayInfo();
        System.out.println("Department: " + department);
        System.out.println("Shift: " + shift);
    }
}
public class CampusSystem {

    static Scanner sc = new Scanner(System.in);

    static Person log[] = new Person[300];
    static int logCount = 0;

    static Student students[] = new Student[300];
    static int stuCount = 0;

    static int wrongID = 0;

    static boolean validIDFormat(String id) {
        if(id.startsWith("ST") && id.length()==8) {
            return id.substring(2).matches("\\d{6}");
        }
        else if(id.startsWith("SF") && id.length()==7) {
            return id.substring(2).matches("\\d{5}");
        }
        return false;
    }

    static boolean after(String t, String lim) { return t.compareTo(lim)>0; }

    static void enterCampus() {
        if(wrongID>=3){
            System.out.println("Entry temporarily locked. Try again later.");
            return;
        }
        System.out.print("Enter ID: ");
        String id=sc.nextLine();

        if(!validIDFormat(id)){
            wrongID++;
            System.out.println("Invalid ID Format.");
            return;
        }
        wrongID=0;

        System.out.print("Enter Name: ");
        String name=sc.nextLine();
        if(name.equals("") || name.matches(".*\\d.*")) {
            System.out.println("Invalid Name.");
            return;
        }

        System.out.print("Enter Role (Student/Staff/Visitor): ");
        String role=sc.nextLine();

        if(role.equalsIgnoreCase("Student")||role.equalsIgnoreCase("Staff")){
            System.out.print("Department: ");
        }
        System.out.print("Entry Time (HH:MM): ");
        String time=sc.nextLine();

        boolean late=false;

        if(role.equalsIgnoreCase("Student")){
            if(after(time,"09:30")){ System.out.println("Late Student"); late=true; }
        }
        else if(role.equalsIgnoreCase("Staff")){
            if(after(time,"08:45")){ System.out.println("Late Staff"); late=true; }
        }
        else if(role.equalsIgnoreCase("Visitor")){
            if(!after(time,"09:59")){
                System.out.println("Visitors allowed only after 10:00");
                return;
            }
        }
        else {
            System.out.println("Access Denied: Unknown Role");
            return;
        }

        log[logCount++] = new Person(name,id,time);

        for(int i=0;i<stuCount;i++){
            if(students[i].id.equals(id)){
                students[i].entryTime=time;
                if(late) students[i].lateCount++;
            }
        }

        System.out.println("Entry Recorded.");
    }

    static void addStudent(){
        System.out.print("Name: "); String name=sc.nextLine();
        System.out.print("ID (STxxxxxx): "); String id=sc.nextLine();
        System.out.print("Age: "); int age=Integer.parseInt(sc.nextLine());
        System.out.print("Department: "); String dept=sc.nextLine();
        System.out.print("Semester: "); int sem=Integer.parseInt(sc.nextLine());

        Student s=new Student(name,id,age,dept,sem);

        System.out.print("Number of subjects (3-5): ");
        int n=Integer.parseInt(sc.nextLine());

        s.marks=new int[n];
        for(int i=0;i<n;i++){
            System.out.print("Marks "+(i+1)+": ");
            s.marks[i]=Integer.parseInt(sc.nextLine());
        }

        s.computeCGPA();
        students[stuCount++]=s;
        System.out.println("Student Added.");
    }

    static void viewStudents(){
        for(int i=0;i<stuCount;i++){
            System.out.println("\n-- Student "+(i+1)+" --");
            students[i].displayInfo();
        }
    }
    static void viewLog(){
        for(int i=0;i<logCount;i++){
            System.out.println("\n-- Log "+(i+1)+" --");
            log[i].displayInfo();
        }
    }
    static void viewLate(){
        for(int i=0;i<stuCount;i++){
            if(students[i].lateCount>0){
                System.out.println("\nLate Student:");
                students[i].displayInfo();
            }
        }
    }
    static void deptSummary(){
        System.out.print("Department: ");
        String d=sc.nextLine();
        int count=0;
        for(int i=0;i<stuCount;i++) if(students[i].department.equalsIgnoreCase(d)) count++;
        System.out.println("Total Students in "+d+": "+count);
    }
    static void searchStudent(){
        System.out.print("Enter ID: ");
        String id=sc.nextLine();

        for(int i=0;i<stuCount;i++){
            if(students[i].id.equals(id)){
                students[i].displayInfo();
                return;
            }
        }
        System.out.println("Student not found.");
    }
    static void top3(){
        System.out.print("Department: ");
        String d=sc.nextLine();

        Student arr[]=new Student[stuCount];
        int c=0;
        for(int i=0;i<stuCount;i++){
            if(students[i].department.equalsIgnoreCase(d))
                arr[c++]=students[i];
        }
        for(int i=0;i<c;i++){
            for(int j=i+1;j<c;j++){
                if(arr[j].cgpa>arr[i].cgpa){
                    Student t=arr[i]; arr[i]=arr[j]; arr[j]=t;
                }
            }
        }

        for(int i=0;i<3 && i<c;i++){
            System.out.println("\nRank "+(i+1)+":");
            arr[i].displayInfo();
        }
    }
    public static void main(String[] args){
        while(true){
            System.out.println("\n1.Enter Campus");
            System.out.println("2.Add Student");
            System.out.println("3.View Students");
            System.out.println("4.View Entry Log");
            System.out.println("5.View Late Students");
            System.out.println("6.Department Summary");
            System.out.println("7.Search Student");
            System.out.println("8.Top 3 Students (Dept)");
            System.out.println("9.Exit");
            System.out.print("Choice: ");

            int ch=Integer.parseInt(sc.nextLine());

            switch(ch){
                case 1: enterCampus(); break;
                case 2: addStudent(); break;
                case 3: viewStudents(); break;
                case 4: viewLog(); break;
                case 5: viewLate(); break;
                case 6: deptSummary(); break;
                case 7: searchStudent(); break;
                case 8: top3(); break;
                case 9: return;
                default: System.out.println("Invalid.");
            }
        }
    }
}

