package org.waspec;

import java.util.Scanner;

public class Program {

    public static void main(String[] args) {


        System.out.println("Welcome to TextExcel");
        Scanner scanner = new Scanner(System.in);

        SpreadSheet spreadSheet = new SpreadSheet();
        while (true) {
            System.out.print("Enter a command:");
            String userInput = scanner.nextLine();
            if (userInput.equals("exit")) {
                break;
            } else{
                    System.out.println(spreadSheet.processCommand(userInput));
            }
        }
        System.out.println("Exiting TextExcel!");
        scanner.close();
    }
}






