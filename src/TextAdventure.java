//Nate Bustos

import java.util.Scanner;

public class TextAdventure {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Text Adventure Game!");
        System.out.println("Type commands like GO, LOOK, GET, DROP, INVENTORY, QUIT");
        System.out.println();

        // Temporary simple loop until Game.java is finished
        boolean running = true;

        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine();

            // Basic command handling (temporary)
            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                running = false;
            } else {
                System.out.println("You typed: " + input);
                System.out.println("(Game logic not connected yet)");
            }
        }

        scanner.close();
    }
}
