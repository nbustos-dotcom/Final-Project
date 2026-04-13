import java.util.Scanner;

public class TextAdventure {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game game = new Game();

        game.startGame();

        while (game.isRunning()) {
            System.out.print("> ");
            String input = scanner.nextLine();
            game.processCommand(input);
        }

        scanner.close();
    }
}