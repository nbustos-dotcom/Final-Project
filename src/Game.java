// William Ingels
import java.util.Scanner;

public class Game {

    private Scanner input;

    // Current room
    private Room currentRoom;

    // Room objects
    private Room darkCell;
    private Room redRoom;
    private Room whiteRoom;
    private Room blueRoom;
    private Room nextRoom;
    private Room greyRoom;
    private Room lobbyRoom;
    private Room lionRoom;
    private Room supplyCloset;
    private Room wireRoom;

    // Item objects
    private Item flashlight;
    private Item book;
    private Item key;
    private Item deadBody;

    // Game state
    private boolean hasFlashlight = false;
    private boolean hasKey = false;
    private boolean running = true;

    // White room puzzle state
    private boolean hasCup = false;
    private boolean cupFilled = false;
    private boolean toiletFlushed = false;
    private boolean tankOpened = false;
    private boolean hasMagnetChain = false;
    private boolean tubTurnedOn = false;
    private boolean hasDrainKey = false;

    // Blue room puzzle state
    private boolean blueRoomEntered = false;
    private boolean chestOpened = false;
    private boolean hasHammer = false;
    private boolean tankBroken = false;
    private boolean hasCrank = false;

    // Grey room state
    private boolean riddlePassed = false;
    private boolean scannerUnlocked = false;

    // Lobby / lion / closet state
    private boolean hasKnife = false;
    private boolean knifeBroken = false;
    private boolean hasLionKeycard = false;
    private boolean hasPliers = false;
    private boolean toolboxOpened = false;
    private boolean greenMessageShown = false;

    public Game() {
        input = new Scanner(System.in);
        setUpGame();
    }

    // Creates rooms and items
    private void setUpGame() {
        darkCell = new Room(
                "Dark Cell",
                "You wake up in a dark prison cell. The air is cold and damp. " +
                "There is a flashlight on the floor, an old book on a small table, " +
                "a dead body in the corner, and a locked door with a keyhole."
        );

        redRoom = new Room(
                "Red Padded Room",
                "You step into a red padded room. Even the floor is padded. " +
                "When you walk on it or press into the walls, it lets out a mushy, moist noise, " +
                "and a strange red liquid slowly oozes out. A single hanging light swings above you. " +
                "Across the room is a dark wooden door with a square crank hole. " +
                "To your right is a white door. To your left is a blue door."
        );

        whiteRoom = new Room(
                "White Bathroom",
                "You enter a room so white it is almost blinding. LED bulbs line the ceiling " +
                "and give off a mind-numbing buzzing sound. The room is a small bathroom with a sink, " +
                "a bathtub, and a toilet, all perfectly clean."
        );

        blueRoom = new Room(
                "Blue Room",
                "The room is dark and blue. Once light fills it, you can see twelve decorated glass fish tanks " +
                "full of sea life. In the center on the far side is a special fish tank with a strange metal object inside. " +
                "In the right corner sits a small pirate chest with a keyhole."
        );

        nextRoom = new Room(
                "Cold Dark Room",
                "A freezing darkness surrounds you."
        );

        greyRoom = new Room(
                "Grey Room",
                "The lights hum to life, revealing a plain grey room. " +
                "At the far end is a metal door. In one corner is a strange giant hole in the floor."
        );

        lobbyRoom = new Room(
                "Lobby",
                "You step into a surprisingly nice lobby-looking room with a front desk, couches, chairs, and a skylight. " +
                "Across the room is a door with an exit sign above it and a keycard slot next to it. " +
                "To the right is a purple door. To the left is a simple wooden door."
        );

        lionRoom = new Room(
                "Lion Room",
                "You open the purple door and see a large cage with a gate. " +
                "Inside is a massive lion, and behind it is a dead body. " +
                "In the dead body's hand you can see a keycard."
        );

        supplyCloset = new Room(
                "Supply Closet",
                "You open the simple wooden door and find a stuffed supply closet filled with cleaning supplies and tools. " +
                "In one corner is a toolbox. On the wall near the door is a knife."
        );

        wireRoom = new Room(
                "Wire Room",
                "You enter a small room with a table and a door with a window. " +
                "Looking through the window, you can see that this door leads to your escape. " +
                "On the table is a mess of wires attached to some weird metal and a small LED screen."
        );

        flashlight = new Item(
                "flashlight",
                "A small flashlight. It still works.",
                "tool"
        );

        book = new Item(
                "book",
                "An old dusty book. Something looks strange about it.",
                "object"
        );

        key = new Item(
                "key",
                "A small metal key hidden inside the book.",
                "key"
        );

        deadBody = new Item(
                "dead body",
                "A dead body lying in the corner. There is a nametag on it.",
                "object"
        );

        darkCell.addItem(flashlight);
        darkCell.addItem(book);
        darkCell.addItem(deadBody);

        currentRoom = darkCell;
    }

    private void clearScreen() {
        for (int i = 0; i < 40; i++) {
            System.out.println();
        }
    }

    private void pause() {
        System.out.println("\nPress Enter to continue...");
        input.nextLine();
    }

    private void resetGame() {
        hasFlashlight = false;
        hasKey = false;
        running = true;

        hasCup = false;
        cupFilled = false;
        toiletFlushed = false;
        tankOpened = false;
        hasMagnetChain = false;
        tubTurnedOn = false;
        hasDrainKey = false;

        blueRoomEntered = false;
        chestOpened = false;
        hasHammer = false;
        tankBroken = false;
        hasCrank = false;

        riddlePassed = false;
        scannerUnlocked = false;

        hasKnife = false;
        knifeBroken = false;
        hasLionKeycard = false;
        hasPliers = false;
        toolboxOpened = false;
        greenMessageShown = false;

        setUpGame();
    }

    public void startGame() {
        clearScreen();
        System.out.println("=== ESCAPE ROOM ===");
        System.out.println("1. New Game");
        System.out.println("2. Load Save");
        System.out.print("Choose an option: ");

        String choice = input.nextLine();

        clearScreen();

        if (choice.equals("2")) {
            System.out.println("Load save is not ready yet.");
            System.out.println("Starting a new game instead...");
            pause();
        }

        firstRoom();
    }

    // ---------------- FIRST ROOM ----------------
    private void firstRoom() {
        while (running && currentRoom == darkCell) {
            clearScreen();

            System.out.println("=== " + currentRoom.getName() + " ===");
            System.out.println(currentRoom.getDescription());

            System.out.println("\nWhat do you want to do?");
            System.out.println("1. Look at the flashlight");
            System.out.println("2. Check the book");
            System.out.println("3. Check the dead body");
            System.out.println("4. Check the door");
            System.out.println("5. Quit");

            System.out.print("Enter option number: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                lookAtFlashlight();
            } else if (choice.equals("2")) {
                checkBook();
            } else if (choice.equals("3")) {
                checkDeadBody();
            } else if (choice.equals("4")) {
                checkCellDoor();
            } else if (choice.equals("5")) {
                clearScreen();
                System.out.println("Game ended.");
                running = false;
            } else {
                clearScreen();
                System.out.println("Invalid option.");
                pause();
            }
        }
    }

    private void lookAtFlashlight() {
        clearScreen();

        if (!hasFlashlight) {
            System.out.println("You pick up the flashlight. It may help you see better.");
            hasFlashlight = true;
            currentRoom.removeitem("flashlight");
        } else {
            System.out.println("You already picked up the flashlight.");
        }

        pause();
    }

    private void checkBook() {
        clearScreen();
        System.out.println("You open the old book and look through the pages.");

        if (!hasKey) {
            System.out.println("Inside the book, you find a small hidden key.");
            hasKey = true;
        } else {
            System.out.println("There is nothing else inside the book.");
        }

        pause();
    }

    private void checkDeadBody() {
        clearScreen();
        System.out.println("You look at the dead body more closely.");
        System.out.println("The nametag says: Mr. Green.");
        pause();
    }

    private void checkCellDoor() {
        clearScreen();

        if (hasKey) {
            System.out.println("You put the key into the keyhole and unlock the door.");
            System.out.println("The door opens with a slow creak.");
            System.out.println("Beyond it is another room.");
            pause();

            currentRoom = redRoom;
            redRoomLoop();
        } else {
            System.out.println("You try the door, but it is locked.");
            pause();
        }
    }

    // ---------------- RED ROOM ----------------
    private void redRoomLoop() {
        while (running && currentRoom == redRoom) {
            clearScreen();

            System.out.println("=== " + currentRoom.getName() + " ===");
            System.out.println(currentRoom.getDescription());

            System.out.println("\nWhat do you want to do?");
            System.out.println("1. Inspect the padded walls and floor");
            System.out.println("2. Check the dark wooden door");
            System.out.println("3. Go through the white door");
            System.out.println("4. Go through the blue door");
            System.out.println("5. Go back to the dark cell");

            System.out.print("Enter option number: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                inspectRedRoom();
            } else if (choice.equals("2")) {
                checkWoodenDoor();
            } else if (choice.equals("3")) {
                goToWhiteRoom();
            } else if (choice.equals("4")) {
                goToBlueDoor();
            } else if (choice.equals("5")) {
                goBackToCell();
            } else {
                clearScreen();
                System.out.println("Invalid option.");
                pause();
            }
        }
    }

    private void inspectRedRoom() {
        clearScreen();
        System.out.println("You press your hand into the padded wall and step across the floor.");
        System.out.println("Each touch makes a wet, mushy noise.");
        System.out.println("Thick red liquid slowly oozes out from the padding.");
        System.out.println("The single hanging light sways above you, making the room feel even worse.");
        pause();
    }

    private void checkWoodenDoor() {
        clearScreen();

        if (hasCrank) {
            System.out.println("You walk up to the dark wooden door.");
            System.out.println("You place the crank into the square hole and begin to turn it.");
            System.out.println("The mechanism grinds loudly, then the door slowly opens.");
            System.out.println("You step through into the next room.");
            pause();

            currentRoom = nextRoom;
            nextRoomLoop();
        } else {
            System.out.println("You walk up to the dark wooden door.");
            System.out.println("It is locked.");
            System.out.println("Instead of a normal knob, it has a square crank hole attached to it.");
            pause();
        }
    }

    private void goToWhiteRoom() {
        currentRoom = whiteRoom;
        whiteRoomLoop();
    }

    private void goToBlueDoor() {
        clearScreen();

        if (!hasFlashlight) {
            System.out.println("You open the blue door and look inside.");
            System.out.println("The room is completely dark.");
            System.out.println("You can barely tell it is blue from the dim red light behind you.");
            System.out.println("Without a flashlight, it is too dangerous to go in.");
            pause();
            return;
        }

        currentRoom = blueRoom;
        blueRoomLoop();
    }

    private void goBackToCell() {
        currentRoom = darkCell;
    }

    // ---------------- WHITE ROOM ----------------
    private void whiteRoomLoop() {
        while (running && currentRoom == whiteRoom) {
            clearScreen();

            System.out.println("=== " + currentRoom.getName() + " ===");
            System.out.println(currentRoom.getDescription());

            System.out.println("\nWhat do you want to do?");
            System.out.println("1. Inspect the sink");
            System.out.println("2. Inspect the toilet");
            System.out.println("3. Inspect the bathtub");
            System.out.println("4. Go back to the red padded room");

            System.out.print("Enter option number: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                inspectSink();
            } else if (choice.equals("2")) {
                inspectToilet();
            } else if (choice.equals("3")) {
                inspectBathtub();
            } else if (choice.equals("4")) {
                currentRoom = redRoom;
            } else {
                clearScreen();
                System.out.println("Invalid option.");
                pause();
            }
        }
    }

    private void inspectSink() {
        clearScreen();
        System.out.println("You inspect the sink.");
        System.out.println("It is spotless. Sitting near it is a plain cup.");

        if (!hasCup) {
            System.out.println("\n1. Take the cup");
            System.out.println("2. Leave it");
            System.out.print("Choose an option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                hasCup = true;
                System.out.println("You take the cup.");
            } else {
                System.out.println("You leave the cup where it is.");
            }
            pause();
            return;
        }

        if (!cupFilled) {
            System.out.println("\n1. Fill the cup with water");
            System.out.println("2. Leave the sink");
            System.out.print("Choose an option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                cupFilled = true;
                System.out.println("You fill the cup with water.");
            } else {
                System.out.println("You step away from the sink.");
            }
        } else {
            System.out.println("You already have the cup, and it is filled with water.");
        }

        pause();
    }

    private void inspectToilet() {
        clearScreen();
        System.out.println("You inspect the toilet. It is perfectly clean.");

        if (!toiletFlushed) {
            System.out.println("\n1. Flush the toilet");
            System.out.println("2. Leave it alone");
            System.out.print("Choose an option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                toiletFlushed = true;
                System.out.println("You flush the toilet.");
                System.out.println("Inside the tank, something rattles.");
                System.out.println("It almost sounds like a chain.");
            } else {
                System.out.println("You leave the toilet alone.");
            }

            pause();
            return;
        }

        if (!tankOpened) {
            System.out.println("\n1. Open the tank");
            System.out.println("2. Leave it alone");
            System.out.print("Choose an option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                tankOpened = true;
                System.out.println("You lift the tank lid.");
                System.out.println("Inside, you see a hollow tube.");
            } else {
                System.out.println("You leave the toilet alone.");
            }

            pause();
            return;
        }

        if (tankOpened && !hasMagnetChain) {
            System.out.println("Inside the tank is the hollow tube.");

            if (cupFilled) {
                System.out.println("\n1. Pour the cup of water into the tube");
                System.out.println("2. Leave it alone");
                System.out.print("Choose an option: ");
                String choice = input.nextLine();

                if (choice.equals("1")) {
                    hasMagnetChain = true;
                    cupFilled = false;
                    System.out.println("You pour the water into the tube.");
                    System.out.println("A hidden chain rises up from inside it.");
                    System.out.println("Attached to the end is a magnet.");
                    System.out.println("You take the chain and magnet.");
                } else {
                    System.out.println("You leave the tank alone.");
                }
            } else {
                System.out.println("It looks like something might happen if liquid was poured into the tube.");
            }

            pause();
            return;
        }

        System.out.println("The toilet tank is open, and you already took the chain with the magnet.");
        pause();
    }

    private void inspectBathtub() {
        clearScreen();
        System.out.println("You inspect the bathtub.");

        if (!tubTurnedOn) {
            System.out.println("\n1. Turn the bathtub on for a second");
            System.out.println("2. Leave it alone");
            System.out.print("Choose an option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                tubTurnedOn = true;
                System.out.println("You turn the water on for a moment, then shut it back off.");
                System.out.println("The water drains very slowly.");
                System.out.println("It looks like something is stuck in the drain.");
            } else {
                System.out.println("You leave the bathtub alone.");
            }

            pause();
            return;
        }

        if (tubTurnedOn && !hasDrainKey) {
            if (hasMagnetChain) {
                System.out.println("\n1. Lower the magnet on the chain into the drain");
                System.out.println("2. Leave the bathtub alone");
                System.out.print("Choose an option: ");
                String choice = input.nextLine();

                if (choice.equals("1")) {
                    hasDrainKey = true;
                    System.out.println("You lower the magnet into the drain and slowly pull it back up.");
                    System.out.println("A key is attached to it.");
                    System.out.println("You take the key.");
                } else {
                    System.out.println("You leave the bathtub alone.");
                }
            } else {
                System.out.println("Something is definitely stuck in the drain.");
                System.out.println("You might need some way to pull it out.");
            }

            pause();
            return;
        }

        System.out.println("The drain is clear now. You already took the key from it.");
        pause();
    }

    // ---------------- BLUE ROOM ----------------
    private void blueRoomLoop() {
        while (running && currentRoom == blueRoom) {
            clearScreen();

            if (!blueRoomEntered) {
                System.out.println("=== Blue Room Entrance ===");
                System.out.println("The second you step in, the room is almost completely dark.");
                System.out.println("You can only tell the room is blue from the dim red light behind you.");
                System.out.println("You switch on your flashlight and step farther inside.");
                System.out.println("The beam reveals twelve glass fish tanks, all decorated and full of sea life.");
                System.out.println("On the far side in the center is a fish tank with a strange metal object inside.");
                System.out.println("To your right in the corner is a small pirate chest with a keyhole.");
                blueRoomEntered = true;
                pause();
            }

            clearScreen();
            System.out.println("=== " + currentRoom.getName() + " ===");
            System.out.println(currentRoom.getDescription());

            System.out.println("\nWhat do you want to do?");
            System.out.println("1. Inspect the special fish tank");
            System.out.println("2. Inspect the pirate chest");
            System.out.println("3. Go back to the red padded room");

            System.out.print("Enter option number: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                inspectFishTank();
            } else if (choice.equals("2")) {
                inspectPirateChest();
            } else if (choice.equals("3")) {
                currentRoom = redRoom;
            } else {
                clearScreen();
                System.out.println("Invalid option.");
                pause();
            }
        }
    }

    private void inspectFishTank() {
        clearScreen();

        if (!tankBroken) {
            System.out.println("You walk up to the special fish tank.");
            System.out.println("Inside, you can now clearly see a crank handle.");
            System.out.println("It looks like it would fit perfectly into the square hole on the wooden door in the red room.");
            System.out.println("But there does not seem to be any safe way to open the tank.");

            if (hasHammer) {
                System.out.println("\n1. Break the fish tank with the hammer");
                System.out.println("2. Leave it alone");
                System.out.print("Choose an option: ");
                String choice = input.nextLine();

                if (choice.equals("1")) {
                    tankBroken = true;
                    hasCrank = true;

                    System.out.println("You swing the hammer into the glass.");
                    System.out.println("The special tank shatters.");
                    System.out.println("For some reason, the other fish tanks all crack and explode too.");
                    System.out.println("Water rushes across the room as the tanks empty all at once.");
                    System.out.println("You nearly drown, but barely manage to swim back toward the red room.");
                    System.out.println("You throw yourself through the doorway just as the blue room door slams shut behind you.");
                    System.out.println("You look down and see the crank lying next to you.");
                    System.out.println("You pick up the crank.");
                    pause();

                    currentRoom = redRoom;
                } else {
                    System.out.println("You step away from the tank.");
                    pause();
                }
            } else {
                System.out.println("You do not have any way to break the glass.");
                pause();
            }

            return;
        }

        System.out.println("The blue room is flooded and sealed shut now.");
        System.out.println("You already got the crank out.");
        pause();
    }

    private void inspectPirateChest() {
        clearScreen();

        if (!hasDrainKey) {
            System.out.println("You kneel by the pirate chest.");
            System.out.println("It is locked.");
            System.out.println("You need a key to open it.");
            pause();
            return;
        }

        if (!chestOpened) {
            System.out.println("You use the key from the bathtub drain to unlock the chest.");
            System.out.println("Inside is a hammer.");
            chestOpened = true;

            System.out.println("\n1. Take the hammer");
            System.out.println("2. Leave it");
            System.out.print("Choose an option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                hasHammer = true;
                System.out.println("You take the hammer.");
            } else {
                System.out.println("You leave the hammer in the chest.");
            }

            pause();
            return;
        }

        if (!hasHammer) {
            System.out.println("The chest is open. The hammer is still inside.");

            System.out.println("\n1. Take the hammer");
            System.out.println("2. Leave it");
            System.out.print("Choose an option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                hasHammer = true;
                System.out.println("You take the hammer.");
            } else {
                System.out.println("You leave the hammer in the chest.");
            }

            pause();
            return;
        }

        System.out.println("The chest is open and empty.");
        pause();
    }

    // ---------------- COLD DARK ROOM ----------------
    private void nextRoomLoop() {
        clearScreen();
        System.out.println("=== Cold Dark Room ===");
        System.out.println("You step into a very cold and dark room.");
        System.out.println("It is so dark that you cannot even see your own face in front of you.");
        System.out.println("The door behind you closes and plunges you into total darkness.");
        System.out.println("You try your flashlight, but it only flickers for a second and dies.");
        pause();

        clearScreen();
        System.out.println("Two dark red eyes appear in the darkness in front of you.");
        System.out.println("\"What is the worst university in the entire world?\"");
        System.out.print("Enter your answer: ");

        String answer = input.nextLine().trim();

        if (isCorrectRiddleAnswer(answer)) {
            clearScreen();
            System.out.println("The eyes stare at you for a moment.");
            System.out.println("Then you hear a slithering sound as they dart back and disappear.");
            System.out.println("A second later, the lights turn on.");
            System.out.println("You are now standing in a plain grey room.");
            System.out.println("At the other end is a metal door.");
            System.out.println("In the corner is a strange giant hole.");
            riddlePassed = true;
            currentRoom = greyRoom;
            pause();
            greyRoomLoop();
        } else {
            clearScreen();
            System.out.println("The eyes suddenly lunge forward.");
            System.out.println("A giant snake bursts from the darkness.");
            System.out.println("Before you can react, it wraps around you and eats you.");
            pause();
            deathMenu();
        }
    }

    private boolean isCorrectRiddleAnswer(String answer) {
        String lower = answer.toLowerCase();
        return lower.equals("nmu")
                || lower.equals("northern michigan")
                || lower.equals("northern michigan university");
    }

    // ---------------- GREY ROOM ----------------
    private void greyRoomLoop() {
        while (running && currentRoom == greyRoom) {
            clearScreen();

            System.out.println("=== " + currentRoom.getName() + " ===");
            System.out.println(currentRoom.getDescription());

            System.out.println("\nWhat do you want to do?");
            System.out.println("1. Inspect the metal door");
            System.out.println("2. Inspect the giant hole");
            System.out.println("3. Put your hand on the scanner");

            System.out.print("Enter option number: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                inspectMetalDoor();
            } else if (choice.equals("2")) {
                inspectGiantHole();
            } else if (choice.equals("3")) {
                useHandScanner();
            } else {
                clearScreen();
                System.out.println("Invalid option.");
                pause();
            }
        }
    }

    private void inspectMetalDoor() {
        clearScreen();

        if (scannerUnlocked) {
            System.out.println("You walk up to the metal door.");
            System.out.println("The scanner beside it is green now.");
            System.out.println("The door is unlocked.");

            System.out.println("\n1. Go through the door");
            System.out.println("2. Step away");
            System.out.print("Choose an option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                clearScreen();
                System.out.println("You open the metal door and step into the next room.");
                pause();
                currentRoom = lobbyRoom;
                lobbyLoop();
            } else {
                System.out.println("You step away from the door.");
                pause();
            }
        } else {
            System.out.println("You walk up to the metal door.");
            System.out.println("It is locked.");
            System.out.println("Next to it is a hand scanner.");
            pause();
        }
    }

    private void inspectGiantHole() {
        clearScreen();
        System.out.println("You walk over to the giant hole in the corner.");
        System.out.println("It is deep and dark.");
        System.out.println("You cannot see the bottom.");
        System.out.println("Cold air drifts up from inside it.");
        pause();
    }

    private void useHandScanner() {
        clearScreen();
        System.out.println("You place your hand on the scanner.");
        System.out.println("It beeps, flashes green, and the metal door unlocks.");
        scannerUnlocked = true;
        pause();
    }

    // ---------------- LOBBY ROOM ----------------
    private void lobbyLoop() {
        while (running && currentRoom == lobbyRoom) {
            clearScreen();

            System.out.println("=== " + currentRoom.getName() + " ===");
            System.out.println(currentRoom.getDescription());

            System.out.println("\nWhat do you want to do?");
            System.out.println("1. Inspect the exit door");
            System.out.println("2. Go through the purple door");
            System.out.println("3. Go through the wooden door");
            System.out.println("4. Inspect the front desk");
            System.out.println("5. Inspect the couches and chairs");

            System.out.print("Enter option number: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                inspectExitDoor();
            } else if (choice.equals("2")) {
                goToLionRoom();
            } else if (choice.equals("3")) {
                goToSupplyCloset();
            } else if (choice.equals("4")) {
                inspectFrontDesk();
            } else if (choice.equals("5")) {
                inspectLobbyFurniture();
            } else {
                clearScreen();
                System.out.println("Invalid option.");
                pause();
            }
        }
    }

    private void inspectExitDoor() {
        clearScreen();

        if (hasLionKeycard) {
            System.out.println("You walk up to the door under the exit sign.");
            System.out.println("There is a keycard slot next to it.");

            System.out.println("\n1. Use the keycard");
            System.out.println("2. Step away");
            System.out.print("Choose an option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                System.out.println("You slide the keycard into the reader.");
                System.out.println("The light flashes and the door unlocks.");
                pause();
                currentRoom = wireRoom;
                wireRoomLoop();
            } else {
                System.out.println("You step away from the door.");
                pause();
            }
        } else {
            System.out.println("You walk up to the door under the exit sign.");
            System.out.println("It has a keycard slot next to it.");
            System.out.println("You need a keycard to open it.");
            pause();
        }
    }

    private void goToLionRoom() {
        currentRoom = lionRoom;
        lionRoomLoop();
    }

    private void goToSupplyCloset() {
        currentRoom = supplyCloset;
        supplyClosetLoop();
    }

    private void inspectFrontDesk() {
        clearScreen();
        System.out.println("You inspect the front desk.");
        System.out.println("It looks neat, but there is nothing useful on it.");
        pause();
    }

    private void inspectLobbyFurniture() {
        clearScreen();
        System.out.println("You inspect the couches and chairs.");
        System.out.println("They look clean and comfortable, but there is nothing hidden in them.");
        pause();
    }

    // ---------------- LION ROOM ----------------
    private void lionRoomLoop() {
        while (running && currentRoom == lionRoom) {
            clearScreen();

            System.out.println("=== " + currentRoom.getName() + " ===");
            System.out.println(currentRoom.getDescription());

            System.out.println("\nWhat do you want to do?");
            System.out.println("1. Inspect the lion cage");
            System.out.println("2. Try to enter the cage");
            System.out.println("3. Go back to the lobby");

            System.out.print("Enter option number: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                inspectLionCage();
            } else if (choice.equals("2")) {
                enterLionCage();
            } else if (choice.equals("3")) {
                currentRoom = lobbyRoom;
            } else {
                clearScreen();
                System.out.println("Invalid option.");
                pause();
            }
        }
    }

    private void inspectLionCage() {
        clearScreen();
        System.out.println("You look closely at the large cage.");
        System.out.println("Inside is a massive lion pacing back and forth.");
        System.out.println("Behind it is a dead body.");
        System.out.println("In the dead body's hand, you can clearly see a keycard.");
        pause();
    }

    private void enterLionCage() {
        clearScreen();

        if (hasLionKeycard) {
            System.out.println("You already got the keycard from the cage.");
            pause();
            return;
        }

        if (hasKnife) {
            System.out.println("You grip the knife and step into the cage.");
            System.out.println("The lion lunges at you.");
            System.out.println("You slash at it and manage to hurt it just enough to get past.");
            System.out.println("You grab the keycard from the dead body's hand and run for your life.");
            System.out.println("You barely escape the cage.");
            System.out.println("As you escape, the knife snaps and breaks.");
            hasLionKeycard = true;
            hasKnife = false;
            knifeBroken = true;
            pause();
            return;
        }

        System.out.println("You step into the cage.");
        System.out.println("The lion instantly attacks.");
        System.out.println("Before you can get away, it kills you.");
        pause();
        deathMenu();
    }

    // ---------------- SUPPLY CLOSET ----------------
    private void supplyClosetLoop() {
        while (running && currentRoom == supplyCloset) {
            clearScreen();

            System.out.println("=== " + currentRoom.getName() + " ===");
            System.out.println(currentRoom.getDescription());

            System.out.println("\nWhat do you want to do?");
            System.out.println("1. Inspect the toolbox");
            System.out.println("2. Inspect the knife on the wall");
            System.out.println("3. Go back to the lobby");

            System.out.print("Enter option number: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                inspectToolbox();
            } else if (choice.equals("2")) {
                inspectWallKnife();
            } else if (choice.equals("3")) {
                currentRoom = lobbyRoom;
            } else {
                clearScreen();
                System.out.println("Invalid option.");
                pause();
            }
        }
    }

    private void inspectToolbox() {
        clearScreen();

        if (!toolboxOpened) {
            System.out.println("You open the toolbox.");
            System.out.println("Inside you find a pair of pliers.");
            toolboxOpened = true;
        } else {
            System.out.println("The toolbox is already open.");
            System.out.println("Inside is a pair of pliers.");
        }

        if (!hasPliers) {
            System.out.println("\n1. Take the pliers");
            System.out.println("2. Leave them");
            System.out.print("Choose an option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                hasPliers = true;
                System.out.println("You take the pliers.");
            } else {
                System.out.println("You leave the pliers in the toolbox.");
            }
        } else {
            System.out.println("You already took the pliers.");
        }

        pause();
    }

    private void inspectWallKnife() {
        clearScreen();

        if (!hasKnife && !knifeBroken) {
            System.out.println("A knife hangs on the wall near the door.");

            System.out.println("\n1. Take the knife");
            System.out.println("2. Leave it");
            System.out.print("Choose an option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                hasKnife = true;
                System.out.println("You take the knife.");
            } else {
                System.out.println("You leave the knife on the wall.");
            }
        } else if (hasKnife) {
            System.out.println("You already took the knife.");
        } else {
            System.out.println("The knife used to hang here, but it broke when you used it on the lion.");
        }

        pause();
    }

    // ---------------- WIRE ROOM ----------------
    private void wireRoomLoop() {
        while (running && currentRoom == wireRoom) {
            clearScreen();

            System.out.println("=== " + currentRoom.getName() + " ===");
            System.out.println(currentRoom.getDescription());

            System.out.println("\nWhat do you want to do?");
            System.out.println("1. Inspect the escape door");
            System.out.println("2. Inspect the table with wires");
            System.out.println("3. Go back to the lobby");

            System.out.print("Enter option number: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                inspectEscapeDoor();
            } else if (choice.equals("2")) {
                inspectWireTable();
            } else if (choice.equals("3")) {
                currentRoom = lobbyRoom;
            } else {
                clearScreen();
                System.out.println("Invalid option.");
                pause();
            }
        }
    }

    private void inspectEscapeDoor() {
        clearScreen();
        System.out.println("You look through the window in the door.");
        System.out.println("You can see that this door leads to your escape.");
        System.out.println("But right now it will not open.");
        pause();
    }

    private void inspectWireTable() {
        clearScreen();
        System.out.println("You inspect the table.");
        System.out.println("There is a mess of wires attached to some weird metal and a small LED screen.");

        if (hasPliers && !greenMessageShown) {
            System.out.println("\n1. Use the pliers on one of the wires");
            System.out.println("2. Leave it alone");
            System.out.print("Choose an option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                greenMessageShown = true;
                System.out.println("You use the pliers on one of the wires.");
                System.out.println("The machine sparks for a second.");
                System.out.println("A message appears on the LED screen:");
                System.out.println("\"The key is green\"");
            } else {
                System.out.println("You leave the wires alone.");
            }
        } else if (greenMessageShown) {
            System.out.println("The LED screen is on.");
            System.out.println("It says: \"The key is green\"");
        } else {
            System.out.println("You might need a tool to safely mess with the wires.");
        }

        pause();
    }

    // ---------------- DEATH / RESTART ----------------
    private void deathMenu() {
        while (true) {
            clearScreen();
            System.out.println("=== YOU DIED ===");
            System.out.println("1. Restart");
            System.out.println("2. Load Save");
            System.out.print("Choose an option: ");

            String choice = input.nextLine();

            if (choice.equals("1")) {
                resetGame();
                startGame();
                return;
            } else if (choice.equals("2")) {
                clearScreen();
                System.out.println("Load save is not ready yet.");
                System.out.println("Restarting new game instead...");
                pause();
                resetGame();
                startGame();
                return;
            } else {
                System.out.println("Invalid option.");
                pause();
            }
        }
    }
}
