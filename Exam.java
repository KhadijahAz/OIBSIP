import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import Exam.StdData.Std;

public class Exam {
    public static class Student {
        private String StudentID;
        private String password;
        
        public Student() {}

        public String gettingStudentID() {
            return StudentID;
        }

        public void settingStudentID(String StudentID) {
            this.StudentID = StudentID;
        }

        public String gettingPassword() {
            return password;
        }

        public void settingPassword(String password) {
            this.password = password;
        }


        public void updatingProfile(String newStudentID) {
            this.StudentID = newStudentID;
            
        }
    }

    public static class StdData {
        private HashMap<String, Student> stds;

        public StdData() {
            stds = new HashMap<>();
        }

        public void addingStd(Student std) {
            stds.put(std.gettingStudentID(), std);
        }

        public Student gettingStdByStudentID(String StudentID) {
            return stds.get(StudentID);
        }
    }

    public static class MCQQues {
        private String ques;
        private List<String> options;
        private int correctAnswer;

        public MCQQues(String ques, List<String> options, int correctAnswer) {
            this.ques = ques;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }

        public String gettingaQuestion() {
            return ques;
        }

        public List<String> gettingAllOptions() {
            return options;
        }

        public int gettingCorrectAnswer() {
            return correctAnswer;
        }

        public boolean isSelectedAnswerCorrect(int selectedAnswer) {
            return selectedAnswer == correctAnswer;
        }
    }

    public static class ExamDurationTimer {
        private int timeInSeconds;
        private Timer timer;
        private boolean istheTimeUp;

        public ExamDurationTimer(int timeInSeconds) {
            this.timeInSeconds = timeInSeconds;
            this.istheTimeUp = false;
        }

        public void startTimer() {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    istheTimeUp = true;
                    timer.cancel();
                }
            }, timeInSeconds * 1000);
        }

        public boolean istheTimeUp() {
            return istheTimeUp;
        }
    }

    public static class ExamSession {
        private String generatingsessionId;
        private Student std;

        public ExamSession(Student std) {
            this.generatingsessionId = UUID.randomUUID().toString();
            this.std = std;
        }

        public String gettingSessionId() {
            return generatingsessionId;
        }

        public Student gettingStudent() {
            return std;
        }
    }

    public static void main(String[] args) {
        StdData StdDB = new StdData();
    
        Student student1 = new Student();
        student1.settingStudentID("Arnold789");
        student1.settingPassword("arny123");
        StdDB.addingStd(student1);

        Student student2 = new Student();
        student2.settingStudentID("Jamie678");
        student2.settingPassword("13579");
        StdDB.addingStd(student2);

        Scanner scanner = new Scanner(System.in);

        //Welcome message
        System.out.println("Welcome to your Online Examination!");

        System.out.println("\nEnter your StudentID:");
        String StudentID = scanner.nextLine();
        System.out.println("\nEnter your password:");
        String password = scanner.nextLine();

        Student std = StdDB.gettingStdByStudentID(StudentID);

        if (std != null && std.gettingPassword().equals(password)) {
            System.out.println("\nLogin successful! Welcome, " + StudentID);
            
            //MCQ questions
            List<MCQQues> mcqQues = new ArrayList<>();

            List<String> options1 = new ArrayList<>();
            options1.add("a) Mercury");
            options1.add("b) Venus");
            options1.add("c) Earth");
            options1.add("d) Mars");
            MCQQues question1 = new MCQQues("Which planet is known as the \"Blue Planet\"?", options1, 2);
            mcqQues.add(question1);

            List<String> options2 = new ArrayList<>();
            options2.add("a) Mercury");
            options2.add("b) Venus");
            options2.add("c) Mars");
            options2.add("d) Jupiter");
            MCQQues question2 = new MCQQues("The largest volcano in the solar system, \"Olympus Mons,\" is located on which planet?", options2, 2);
            mcqQues.add(question2);

            ExamDurationTimer timer = new ExamDurationTimer(300); // 300 seconds (5 minutes)
            timer.startTimer();

            ExamSession session = new ExamSession(std);
            System.out.println("\nSession ID: " + session.gettingSessionId());

boolean running = true;
while (running) {
    System.out.println("\n-----Main Menu:-----");
    System.out.println("       1. Start Exam");
    System.out.println("       2. Change Password");
    System.out.println("       3. Logout and Exit");

    System.out.println("Please enter your choice:");
    String option = scanner.nextLine();

    switch (option) {
        case "1":
            startingExam(mcqQues, timer, session, scanner);
            break;
        case "2":
            changingPassword(std, scanner);
            break;
        case "3":
            running = false;
            System.out.println("Logging out. Goodbye, " + StudentID + "!");
            break;
        default:
            System.out.println("Invalid option. Please try again.");
            break;
    }
}

System.exit(0); // Exiting the program 


            // Implementing the examination logic
            int examscore = 0;
            for (MCQQues question : mcqQues) {
                System.out.println(question.gettingaQuestion());
                List<String> options = question.gettingAllOptions();
                for (String option : options) {
                    System.out.println(option);
                }

                System.out.println("Enter your answer (a, b, c, d):");
                String StdAnswer = scanner.nextLine().toLowerCase();

                switch (StdAnswer) {
                    case "a":
                    case "b":
                    case "c":
                    case "d":
                        int selectedAnswer = StdAnswer.charAt(0) - 'a';
                        if (question.isSelectedAnswerCorrect(selectedAnswer)) {
                            System.out.println("Correct answer!");
                            examscore++;
                        } else {
                            System.out.println("Incorrect answer!");
                        }
                        break;
                    default:
                        System.out.println("Invalid answer!");
                        break;
                }
            }

            if (timer.istheTimeUp()) {
                System.out.println("Time's up! Your answers will be auto-submitted.");
            } else {
                System.out.println("Congratulations! You've completed the exam!");
            }

            System.out.println("Bravo! You have scored: " + examscore + " out of " + mcqQues.size());

            System.out.println("Do you want to update your Student profile? (yes/no):");
            String updatingChoice = scanner.nextLine().toLowerCase();
            if (updatingChoice.equals("yes")) {
                // Update password
                System.out.println("Enter your new password:");
                String newPassword = scanner.nextLine();
                std.settingPassword(newPassword);
                System.out.println("Profile information updated successfully!");
            }

            System.out.println("Logging out. Goodbye, " + StudentID + "!");
            System.exit(0); 
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private static void startingExam(List<MCQQues> mcqQues, ExamDurationTimer timer, ExamSession session, Scanner scanner) {
        System.out.println("Starting the exam...");
        int examscore = 0;
        
        for (MCQQues question : mcqQues) {
            System.out.println(question.gettingaQuestion());
            List<String> options = question.gettingAllOptions();
            for (String option : options) {
                System.out.println(option);
            }

            System.out.println("Enter your answer (a, b, c, d):");
            String StdAnswer = scanner.nextLine().toLowerCase();

            switch (StdAnswer) {
                case "a":
                case "b":
                case "c":
                case "d":
                    int selectedAnswer = StdAnswer.charAt(0) - 'a';
                    if (question.isSelectedAnswerCorrect(selectedAnswer)) {
                        System.out.println("Correct answer!");
                        examscore++;
                    } else {
                        System.out.println("Incorrect answer!");
                    }
                    break;
                default:
                    System.out.println("Invalid answer!");
                    break;
            }
        }

        if (timer.istheTimeUp()) {
            System.out.println("Time's up! Your answers will be auto-submitted.");
        } else {
            System.out.println("Congratulations! You've completed the exam!");
        }

        System.out.println("Bravo! You have scored: " + examscore + " out of " + mcqQues.size());

        System.out.println("Do you want to update your Student profile? (yes/no):");
        String updatingChoice = scanner.nextLine().toLowerCase();
        if (updatingChoice.equals("yes")) {
        
            System.out.println("Enter your new password:");
            String newPassword = scanner.nextLine();
            session.gettingStudent().settingPassword(newPassword);

            System.out.println("Profile information and password updated successfully!");
        }

        System.out.println("Logging out. Goodbye, " + session.gettingStudent().gettingStudentID() + "!");
        System.exit(0); 
    }

private static void changingPassword(Student std, Scanner scanner) {
    System.out.println("Changing password for Student: " + std.gettingStudentID());
    System.out.println("Enter your new password:");
    String newPassword = scanner.nextLine();
    std.settingPassword(newPassword);
    System.out.println("Password changed successfully!");
}
}

