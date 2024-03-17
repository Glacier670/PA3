import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner myObj = new Scanner(System.in);
        boolean retry = true;
        String inputFile = "";
        boolean txtOrCsv = false;

        while (retry) {
            System.out.println("Hello, please input your .txt or .csv file for conversion.");
            inputFile = myObj.nextLine();
            if (inputFile.endsWith(".txt") || inputFile.endsWith(".csv")) {
                txtOrCsv = inputFile.endsWith(".txt"); // true for .txt, false for .csv
                retry = false;
            } else {
                System.out.println("Invalid file type. Please input a .txt or .csv file.");
            }
        }
        boolean retry2 = true;
        while (retry2) {
            System.out.println("What would you like to do? Type the number corresponding to the desired command");
            System.out.println("1. Convert file");
            System.out.println("2. Normalize file");
            System.out.println("3. Quit");
            int command = myObj.nextInt();
            myObj.nextLine(); // Consume the newline left-over

            switch (command) {
                case 1:
                    FileConversion.convertFile(inputFile, txtOrCsv);
                    System.out.println("File Converted!");
                    break;
                case 2:
                    FileConversion.normalizeFile(inputFile, txtOrCsv);
                    System.out.println("File Normalized!");
                    break;
                case 3:
                    retry2 = false;
                    System.out.println("Thank you!");
                    break;
                default:
                    System.out.println("Invalid command, please try again.");
                    break;
            }
        }
        myObj.close();
    }
}
