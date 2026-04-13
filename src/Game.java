// William Ingels
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Game {

    private Player player;
    private boolean running;

    // Special input states
    private boolean snakeQuestionActive;
    private boolean deathChoiceActive;

    // Prevent double room display
    private boolean roomJustChanged;

    // Rooms
    private Room darkCell;
    private Room redRoom;
    private Room whiteRoom;
    private Room blueRoom;
    private Room coldDarkRoom;
    private Room greyRoom;
    private Room lobbyRoom;
    private Room lionRoom;
    private Room supplyCloset;
    private Room wireRoom;
    private Room escapeRoom;

    // Items
    private Item flashlight;
    private Item book;
    private Item cellKey;
    private Item deadBody;
    private Item cup;
    private Item magnetChain;
    private Item drainKey;
    private Item hammer;
    private Item knife;
    private Item keycard;
    private Item pliers;
    private Item greenEscapeKey;
    private Item crank;

    // Puzzle state
    private boolean keyRevealed;
    private boolean flashlightDead;

    private boolean toiletFlushed;
    private boolean tankOpened;
    private boolean cupFilled;
    private boolean magnetRevealed;
    private boolean bathtubChecked;
    private boolean drainKeyRevealed;

    private boolean chestOpened;
    private boolean blueFlooded;
    private boolean crankTaken;

    private boolean greyDoorUnlocked;

    private boolean knifeBroken;
    private boolean toolboxOpened;
    private boolean keycardTaken;

    private boolean bombStarted;
    private boolean bombKeyRevealed;
    private long bombEndTimeMillis;

    private boolean won;

    public Game() {
        setupWorld();
    }

    public boolean isRunning() {
        return running;
    }

    private void clearScreen() {
        for (int i = 0; i < 45; i++) {
            System.out.println();
        }
    }

    public void startGame() {
        clearScreen();
        System.out.println("Welcome to the Escape Room Game!");
        System.out.println("Type commands like GO, LOOK, GET, DROP, INVENTORY, SAVE, RESTORE, QUIT");
        System.out.println("You can also use some special commands with USE.");
        System.out.println();
        showRoomOptions();
    }

    public void processCommand(String input) {
        if (!running) {
            return;
        }

        roomJustChanged = false;

        if (input == null || input.trim().isEmpty()) {
            clearScreen();
            System.out.println("Type a command.");
            System.out.println();
            if (!deathChoiceActive && !snakeQuestionActive) {
                showRoomOptions();
            }
            return;
        }

        if (deathChoiceActive) {
            clearScreen();
            handleDeathChoice(input.trim());
            return;
        }

        checkBombTimer();
        if (!running || deathChoiceActive) {
            return;
        }

        input = input.trim();

        if (snakeQuestionActive) {
            clearScreen();
            handleSnakeAnswer(input);
            return;
        }

        clearScreen();

        String[] parts = input.split("\\s+", 2);
        String command = parts[0].toUpperCase();
        String target = parts.length > 1 ? parts[1].trim() : "";

        switch (command) {
            case "LOOK":
                handleLook(target);
                break;
            case "GO":
                handleGo(target);
                break;
            case "GET":
                handleGet(target);
                break;
            case "DROP":
                handleDrop(target);
                break;
            case "INVENTORY":
                showInventory();
                break;
            case "SAVE":
                saveGame();
                break;
            case "RESTORE":
                restoreGame();
                return;
            case "QUIT":
                running = false;
                System.out.println("Goodbye!");
                return;
            case "USE":
                handleUse(target);
                break;
            default:
                System.out.println("Unknown command.");
                System.out.println("Try GO, LOOK, GET, DROP, INVENTORY, SAVE, RESTORE, USE, or QUIT.");
                break;
        }

        if (running && !deathChoiceActive && !snakeQuestionActive && !won) {
            System.out.println();
            if (roomJustChanged) {
                showRoomOptions();
            } else {
                showCommandListsOnly();
            }
        }
    }

    private void setupWorld() {
        running = true;
        won = false;
        snakeQuestionActive = false;
        deathChoiceActive = false;
        roomJustChanged = false;

        player = new Player();

        keyRevealed = false;
        flashlightDead = false;

        toiletFlushed = false;
        tankOpened = false;
        cupFilled = false;
        magnetRevealed = false;
        bathtubChecked = false;
        drainKeyRevealed = false;

        chestOpened = false;
        blueFlooded = false;
        crankTaken = false;

        greyDoorUnlocked = false;

        knifeBroken = false;
        toolboxOpened = false;
        keycardTaken = false;

        bombStarted = false;
        bombKeyRevealed = false;
        bombEndTimeMillis = 0L;

        darkCell = new Room(
                "Dark Cell",
                "You wake up in a dark prison cell. The air is cold and damp. " +
                "There is a flashlight on the floor, an old book on a small table, " +
                "and a dead body in the corner near a locked door with a keyhole."
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
                "full of sea life. On the far side is a special fish tank with something strange inside. " +
                "In the corner sits a small pirate chest."
        );

        coldDarkRoom = new Room(
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
                "You step into a nice lobby-looking room with a front desk, couches, chairs, and a skylight. " +
                "Across the room is a door with an EXIT sign above it and a keycard slot next to it. " +
                "To the right is a purple door. To the left is a simple wooden door."
        );

        lionRoom = new Room(
                "Lion Room",
                "You open the purple door and see a large cage. Inside is a massive lion. " +
                "Behind it is a dead body. In the body's hand you can see a keycard."
        );

        supplyCloset = new Room(
                "Supply Closet",
                "You open the wooden door and find a stuffed supply closet filled with cleaning supplies and tools. " +
                "In one corner is a toolbox. On the wall near the door is a knife."
        );

        wireRoom = new Room(
                "Wire Room",
                "You enter a small room with a table and a door with a window. " +
                "Looking through the window, you can see that this door leads to your escape. " +
                "On the table is a mess of wires attached to weird metal and a small LED screen."
        );

        escapeRoom = new Room(
                "Outside",
                "Fresh air hits your face. You made it out alive."
        );

        // Exits
        darkCell.addExit("door", redRoom);

        redRoom.addExit("white", whiteRoom);
        redRoom.addExit("blue", blueRoom);
        redRoom.addExit("cell", darkCell);

        whiteRoom.addExit("red", redRoom);
        whiteRoom.addExit("back", redRoom);

        blueRoom.addExit("red", redRoom);
        blueRoom.addExit("back", redRoom);

        greyRoom.addExit("back", redRoom);

        lobbyRoom.addExit("purple", lionRoom);
        lobbyRoom.addExit("wooden", supplyCloset);
        lobbyRoom.addExit("back", greyRoom);

        lionRoom.addExit("back", lobbyRoom);
        supplyCloset.addExit("back", lobbyRoom);
        wireRoom.addExit("back", lobbyRoom);

        // Items
        flashlight = new Item("flashlight", "A small flashlight. It still works.", "tool");
        book = new Item("book", "An old dusty book. Something looks strange about it.", "object");
        cellKey = new Item("key", "A small metal key hidden inside the book.", "key");
        deadBody = new Item("dead body", "A dead body lying in the corner. There is a nametag on it.", "object");
        cup = new Item("cup", "A plain cup from the sink.", "tool");
        magnetChain = new Item("magnet chain", "A chain with a magnet attached.", "tool");
        drainKey = new Item("drain key", "A key pulled from the bathtub drain.", "key");
        hammer = new Item("hammer", "A sturdy hammer.", "tool");
        knife = new Item("knife", "A sharp knife from the wall.", "weapon");
        keycard = new Item("keycard", "A keycard taken from the dead body behind the lion.", "keycard");
        pliers = new Item("pliers", "A pair of pliers from the toolbox.", "tool");
        greenEscapeKey = new Item("green key", "A green key taken from the dead body.", "key");
        crank = new Item("crank", "A crank handle from the fish tank.", "tool");

        // Starting room items
        darkCell.addItem(flashlight);
        darkCell.addItem(book);
        darkCell.addItem(deadBody);
        whiteRoom.addItem(cup);
        supplyCloset.addItem(knife);

        player.setCurrentRoom(darkCell);
    }

    private void handleLook(String target) {
        Room room = player.getCurrentRoom();

        if (target.isEmpty()) {
            showRoomOptions();
            return;
        }

        String t = target.toLowerCase();

        if (room == darkCell) {
            if (t.equals("book")) {
                System.out.println("You open the old book and look through the pages.");
                if (!keyRevealed) {
                    keyRevealed = true;
                    darkCell.addItem(cellKey);
                    System.out.println("Inside the book, you find a small hidden key.");
                } else {
                    System.out.println("There is nothing else inside the book.");
                }
                return;
            }

            if (t.equals("dead body") || t.equals("body")) {
                System.out.println("You look at the dead body more closely.");
                System.out.println("The nametag says: Mr. Green.");

                if (bombStarted && !bombKeyRevealed) {
                    bombKeyRevealed = true;
                    darkCell.addItem(greenEscapeKey);
                    System.out.println("Tucked into one of its stiff hands is a green key.");
                }
                return;
            }

            if (t.equals("door")) {
                if (hasItem("key")) {
                    System.out.println("The cell door is locked, but you have a key that might open it.");
                } else {
                    System.out.println("The cell door is locked. It has a keyhole.");
                }
                return;
            }
        }

        if (room == redRoom) {
            if (t.equals("walls") || t.equals("wall") || t.equals("floor")) {
                System.out.println("The padded walls and floor make wet, mushy noises.");
                System.out.println("A thick red liquid slowly oozes out of them.");
                return;
            }

            if (t.equals("wooden door") || t.equals("door")) {
                if (crankTaken) {
                    System.out.println("The dark wooden door has a square crank hole. You have the crank.");
                } else {
                    System.out.println("The dark wooden door is locked and has a square crank hole.");
                }
                return;
            }
        }

        if (room == whiteRoom) {
            if (t.equals("sink")) {
                System.out.println("The sink is spotless. A plain cup sits near it.");
                return;
            }

            if (t.equals("toilet")) {
                System.out.println("The toilet is perfectly clean.");
                if (toiletFlushed) {
                    System.out.println("The tank rattled when you flushed it.");
                }
                return;
            }

            if (t.equals("tank")) {
                if (!toiletFlushed) {
                    System.out.println("The toilet tank looks normal. Maybe try flushing first.");
                } else if (!tankOpened) {
                    System.out.println("The tank rattled when flushed. You could try using the tank.");
                } else {
                    System.out.println("Inside the tank you can see a hollow tube.");
                }
                return;
            }

            if (t.equals("bathtub") || t.equals("tub")) {
                System.out.println("The bathtub is perfectly clean.");
                if (bathtubChecked) {
                    System.out.println("The water drains slowly, like something is stuck in the drain.");
                }
                return;
            }
        }

        if (room == blueRoom) {
            if (t.equals("fish tank") || t.equals("tank")) {
                System.out.println("Inside the special fish tank you can see a crank handle.");
                System.out.println("It looks like it fits the wooden door in the red room.");
                if (hasItem("hammer")) {
                    System.out.println("You might be able to break the glass.");
                }
                return;
            }

            if (t.equals("chest") || t.equals("pirate chest")) {
                if (!chestOpened) {
                    System.out.println("The pirate chest is locked.");
                } else {
                    System.out.println("The chest is open.");
                }
                return;
            }
        }

        if (room == coldDarkRoom) {
            System.out.println("It is too dark to see anything.");
            return;
        }

        if (room == greyRoom) {
            if (t.equals("hole")) {
                System.out.println("It is a giant dark hole. You cannot see the bottom.");
                return;
            }

            if (t.equals("metal door") || t.equals("door")) {
                if (greyDoorUnlocked) {
                    System.out.println("The metal door is unlocked.");
                } else {
                    System.out.println("The metal door is locked. There is a hand scanner beside it.");
                }
                return;
            }

            if (t.equals("scanner")) {
                System.out.println("A hand scanner sits beside the metal door.");
                return;
            }
        }

        if (room == lobbyRoom) {
            if (t.equals("front desk")) {
                System.out.println("The front desk looks neat, but there is nothing useful there.");
                return;
            }

            if (t.equals("couches") || t.equals("chairs") || t.equals("couches and chairs")) {
                System.out.println("The couches and chairs look comfortable, but nothing useful is hidden there.");
                return;
            }

            if (t.equals("exit door") || t.equals("door")) {
                if (hasItem("keycard")) {
                    System.out.println("The exit-sign door has a keycard slot. You have a keycard.");
                } else {
                    System.out.println("The exit-sign door has a keycard slot. You need a keycard.");
                }
                return;
            }
        }

        if (room == lionRoom) {
            if (t.equals("lion") || t.equals("cage") || t.equals("lion cage")) {
                System.out.println("A massive lion paces inside the cage.");
                System.out.println("Behind it is a dead body with a keycard in its hand.");
                return;
            }
        }

        if (room == supplyCloset) {
            if (t.equals("toolbox")) {
                System.out.println("You open the toolbox.");
                if (!toolboxOpened) {
                    toolboxOpened = true;
                    supplyCloset.addItem(pliers);
                    System.out.println("Inside you find a pair of pliers.");
                } else {
                    System.out.println("Inside is a pair of pliers.");
                }
                return;
            }

            if (t.equals("knife")) {
                System.out.println("A knife hangs on the wall near the door.");
                return;
            }
        }

        if (room == wireRoom) {
            if (t.equals("table") || t.equals("wires") || t.equals("wire table")) {
                System.out.println("There is a mess of wires attached to weird metal and a small LED screen.");
                if (bombStarted) {
                    System.out.println("The screen says: \"The key is green\"");
                    System.out.println("You need to hurry.");
                }
                return;
            }

            if (t.equals("escape door") || t.equals("door") || t.equals("window")) {
                System.out.println("Looking through the window, you can see this leads to your escape.");
                if (hasItem("green key")) {
                    System.out.println("You now have the right key for it.");
                } else {
                    System.out.println("It is still locked.");
                }
                return;
            }
        }

        Item item = findItemInInventory(target);
        if (item != null) {
            System.out.println(item.getDescription());
            return;
        }

        System.out.println("You do not see anything special about that.");
    }

    private void handleGo(String target) {
        if (target.isEmpty()) {
            System.out.println("Go where?");
            return;
        }

        Room room = player.getCurrentRoom();
        String t = target.toLowerCase();

        if (room == darkCell && t.equals("door")) {
            if (hasItem("key")) {
                player.setCurrentRoom(redRoom);
                roomJustChanged = true;
            } else {
                System.out.println("The door is locked.");
            }
            return;
        }

        if (room == redRoom && (t.equals("wooden") || t.equals("wooden door") || t.equals("door"))) {
            if (crankTaken) {
                player.setCurrentRoom(coldDarkRoom);
                roomJustChanged = true;
                System.out.println("You place the crank into the square hole and turn it.");
                System.out.println("The wooden door opens.");
                enterSnakeRoom();
            } else {
                System.out.println("The wooden door is locked. It needs a crank.");
            }
            return;
        }

        if (room == greyRoom && (t.equals("metal") || t.equals("metal door") || t.equals("door"))) {
            if (greyDoorUnlocked) {
                player.setCurrentRoom(lobbyRoom);
                roomJustChanged = true;
            } else {
                System.out.println("The metal door is locked.");
            }
            return;
        }

        if (room == lobbyRoom && (t.equals("exit") || t.equals("exit door") || t.equals("door"))) {
            if (hasItem("keycard")) {
                player.setCurrentRoom(wireRoom);
                roomJustChanged = true;
            } else {
                System.out.println("The exit-sign door needs a keycard.");
            }
            return;
        }

        if (room == lionRoom && (t.equals("cage") || t.equals("lion cage"))) {
            enterLionCage();
            return;
        }

        if (room == wireRoom && (t.equals("escape") || t.equals("escape door") || t.equals("door"))) {
            if (!bombStarted) {
                System.out.println("The escape door is still locked.");
                return;
            }

            if (hasItem("green key")) {
                player.setCurrentRoom(escapeRoom);
                won = true;
                running = false;
                System.out.println("You unlock the final door with the green key.");
                System.out.println(escapeRoom.getDescription());
                System.out.println("You escape just in time. You win!");
            } else {
                System.out.println("You need the green key first.");
            }
            return;
        }

        if (room == redRoom && t.equals("blue") && blueFlooded) {
            System.out.println("The blue room door is slammed shut now.");
            return;
        }

        Room next = room.getExit(t);
        if (next != null) {
            if (next == blueRoom && !hasItem("flashlight")) {
                System.out.println("The blue room is too dark to safely enter without the flashlight.");
                return;
            }

            player.setCurrentRoom(next);
            roomJustChanged = true;
        } else {
            System.out.println("You cannot go there.");
        }
    }

    private void handleGet(String target) {
        if (target.isEmpty()) {
            System.out.println("Get what?");
            return;
        }

        Room room = player.getCurrentRoom();
        Item item = room.removeitem(target);

        if (item == null) {
            System.out.println("That item is not here.");
            return;
        }

        if (item.getType().equalsIgnoreCase("object")) {
            room.addItem(item);
            System.out.println("You cannot carry that.");
            return;
        }

        player.getItem(item);

        if (item.getName().equalsIgnoreCase("keycard")) {
            keycardTaken = true;
        }

        System.out.println("You picked up the " + item.getName() + ".");
    }

    private void handleDrop(String target) {
        if (target.isEmpty()) {
            System.out.println("Drop what?");
            return;
        }

        Item item = removeInventoryItem(target);
        if (item == null) {
            System.out.println("You do not have that item.");
            return;
        }

        if (item.getName().equalsIgnoreCase("flashlight")) {
            System.out.println("You probably should keep that.");
            player.getItem(item);
            return;
        }

        player.getCurrentRoom().addItem(item);

        if (item.getName().equalsIgnoreCase("cup")) {
            cupFilled = false;
        }

        System.out.println("You dropped the " + item.getName() + ".");
    }

    private void handleUse(String target) {
        if (target.isEmpty()) {
            System.out.println("Use what?");
            return;
        }

        Room room = player.getCurrentRoom();
        String t = target.toLowerCase();

        if (room == whiteRoom && t.equals("sink")) {
            if (hasItem("cup")) {
                cupFilled = true;
                System.out.println("You fill the cup with water.");
            } else {
                System.out.println("You need the cup first.");
            }
            return;
        }

        if (room == whiteRoom && t.equals("toilet")) {
            if (!toiletFlushed) {
                toiletFlushed = true;
                System.out.println("You flush the toilet.");
                System.out.println("The tank rattles, almost like a chain sound.");
            } else {
                System.out.println("You already flushed the toilet.");
            }
            return;
        }

        if (room == whiteRoom && t.equals("tank")) {
            if (!toiletFlushed) {
                System.out.println("You should flush the toilet first.");
            } else if (!tankOpened) {
                tankOpened = true;
                System.out.println("You open the tank and see a hollow tube inside.");
            } else {
                System.out.println("The tank is already open.");
            }
            return;
        }

        if (room == whiteRoom && (t.equals("tube") || t.equals("cup tube") || t.equals("cup to tube"))) {
            if (!tankOpened) {
                System.out.println("You need to open the toilet tank first.");
            } else if (!hasItem("cup")) {
                System.out.println("You need the cup.");
            } else if (!cupFilled) {
                System.out.println("The cup is empty.");
            } else if (!magnetRevealed) {
                magnetRevealed = true;
                whiteRoom.addItem(magnetChain);
                cupFilled = false;
                System.out.println("You pour the water into the tube.");
                System.out.println("A hidden chain rises up from inside it.");
                System.out.println("A magnet is attached to the end.");
            } else {
                System.out.println("You already pulled the chain up.");
            }
            return;
        }

        if (room == whiteRoom && (t.equals("bathtub") || t.equals("tub"))) {
            if (!bathtubChecked) {
                bathtubChecked = true;
                System.out.println("You turn the bathtub on for a second and then turn it off.");
                System.out.println("The water drains very slowly, like something is stuck in the drain.");
            } else {
                System.out.println("The drain still looks clogged.");
            }
            return;
        }

        if (room == whiteRoom && (t.equals("drain") || t.equals("magnet drain") || t.equals("magnet on drain"))) {
            if (!bathtubChecked) {
                System.out.println("You have no reason to do that yet.");
            } else if (!hasItem("magnet chain")) {
                System.out.println("You need the magnet chain.");
            } else if (!drainKeyRevealed) {
                drainKeyRevealed = true;
                whiteRoom.addItem(drainKey);
                System.out.println("You lower the magnet into the drain and pull up a key.");
            } else {
                System.out.println("You already pulled something out of the drain.");
            }
            return;
        }

        if (room == blueRoom && t.equals("chest")) {
            if (!hasItem("drain key")) {
                System.out.println("The chest is locked.");
            } else if (!chestOpened) {
                chestOpened = true;
                blueRoom.addItem(hammer);
                System.out.println("You unlock the pirate chest.");
                System.out.println("Inside is a hammer.");
            } else {
                System.out.println("The chest is already open.");
            }
            return;
        }

        if (room == blueRoom && (t.equals("tank") || t.equals("hammer tank") || t.equals("hammer fish tank"))) {
            if (!hasItem("hammer")) {
                System.out.println("You do not have anything to break the tank with.");
            } else if (!blueFlooded) {
                blueFlooded = true;
                crankTaken = true;
                player.getItem(crank);
                player.setCurrentRoom(redRoom);
                roomJustChanged = true;
                System.out.println("You smash the fish tank with the hammer.");
                System.out.println("The rest of the tanks break too and flood the room.");
                System.out.println("You barely escape back to the red room.");
                System.out.println("The blue door slams shut behind you.");
                System.out.println("You notice the crank beside you and take it.");
            } else {
                System.out.println("The room is already flooded and sealed.");
            }
            return;
        }

        if (room == greyRoom && (t.equals("scanner") || t.equals("hand scanner"))) {
            greyDoorUnlocked = true;
            System.out.println("You place your hand on the scanner.");
            System.out.println("It flashes green and unlocks the metal door.");
            return;
        }

        if (room == lobbyRoom && (t.equals("keycard") || t.equals("keycard exit"))) {
            if (hasItem("keycard")) {
                player.setCurrentRoom(wireRoom);
                roomJustChanged = true;
                System.out.println("You slide the keycard through the reader.");
            } else {
                System.out.println("You do not have a keycard.");
            }
            return;
        }

        if (room == wireRoom && (t.equals("wires") || t.equals("pliers wires") || t.equals("pliers on wires"))) {
            if (!hasItem("pliers")) {
                System.out.println("You need the pliers.");
            } else if (!bombStarted) {
                bombStarted = true;
                bombEndTimeMillis = System.currentTimeMillis() + 120000L;
                System.out.println("You use the pliers on one of the wires.");
                System.out.println("The machine sparks.");
                System.out.println("The LED screen lights up and says: \"The key is green\"");
                System.out.println("A timer starts counting down from 2 minutes.");
                System.out.println("You need to get back to the first room, check the dead body, get the green key, and escape.");
                showBombTimeRemaining();
            } else {
                System.out.println("The screen still says: \"The key is green\"");
                showBombTimeRemaining();
            }
            return;
        }

        if (room == lionRoom && (t.equals("lion") || t.equals("knife lion") || t.equals("knife on lion"))) {
            enterLionCage();
            return;
        }

        System.out.println("Nothing happens.");
    }

    private void handleSnakeAnswer(String input) {
        String answer = input.trim().toLowerCase();

        if (answer.equals("nmu")
                || answer.equals("northern michigan")
                || answer.equals("northern michigan university")) {
            snakeQuestionActive = false;
            player.setCurrentRoom(greyRoom);
            roomJustChanged = true;
            System.out.println("You hear a slithering sound as the eyes dart back and disappear.");
            System.out.println("The lights turn on.");
            showRoomOptions();
        } else {
            System.out.println("The eyes lunge forward.");
            System.out.println("A giant snake bursts from the darkness and eats you.");
            triggerDeath();
        }
    }

    private void handleDeathChoice(String input) {
        String choice = input.trim().toLowerCase();

        if (choice.equals("restart") || choice.equals("1")) {
            setupWorld();
            startGame();
        } else if (choice.equals("load") || choice.equals("load save") || choice.equals("2")) {
            restoreGame();
        } else {
            System.out.println("Type RESTART or LOAD.");
        }
    }

    private void enterSnakeRoom() {
        snakeQuestionActive = true;
        flashlightDead = true;
        System.out.println("You step into a very cold and dark room.");
        System.out.println("It is so dark that you cannot even see your own face in front of you.");
        System.out.println("The door behind you closes and plunges you into total darkness.");
        System.out.println("You try your flashlight but it flickers for a second and dies.");
        System.out.println("Suddenly a pair of dark red eyes appear in front of you.");
        System.out.println("\"What is the worst university in the entire world?\"");
        System.out.println("Type your answer.");
    }

    private void enterLionCage() {
        if (keycardTaken) {
            System.out.println("You already grabbed the keycard.");
            return;
        }

        if (hasItem("knife")) {
            removeInventoryItem("knife");
            knifeBroken = true;
            keycardTaken = true;
            lionRoom.addItem(keycard);
            System.out.println("You rush into the cage with the knife.");
            System.out.println("You manage to hurt the lion just enough to grab the keycard.");
            System.out.println("You barely escape, but the knife breaks.");
        } else {
            System.out.println("You try to go into the cage.");
            System.out.println("The lion kills you.");
            triggerDeath();
        }
    }

    private void triggerDeath() {
        deathChoiceActive = true;
        System.out.println();
        System.out.println("You died.");
        System.out.println("Type RESTART or LOAD.");
    }

    private void checkBombTimer() {
        if (!bombStarted || won || deathChoiceActive) {
            return;
        }

        long timeLeft = bombEndTimeMillis - System.currentTimeMillis();
        if (timeLeft <= 0) {
            bombStarted = false;
            clearScreen();
            System.out.println("The timer reaches zero.");
            System.out.println("The bomb goes off.");
            triggerDeath();
        }
    }

    private void showBombTimeRemaining() {
        if (!bombStarted) {
            return;
        }

        long timeLeft = bombEndTimeMillis - System.currentTimeMillis();
        if (timeLeft < 0) {
            timeLeft = 0;
        }

        long seconds = timeLeft / 1000;
        long minutesPart = seconds / 60;
        long secondsPart = seconds % 60;

        System.out.println("Time left: " + minutesPart + ":" + String.format("%02d", secondsPart));
    }

    private void showRoomOptions() {
        checkBombTimer();
        if (!running || deathChoiceActive) {
            return;
        }

        Room room = player.getCurrentRoom();
        System.out.println(room.getDescription());
        System.out.println();
        showCommandListsOnly();
    }

    private void showCommandListsOnly() {
        Room room = player.getCurrentRoom();

        if (bombStarted) {
            showBombTimeRemaining();
        }

        if (room == darkCell) {
            System.out.println("LOOK: flashlight, book, dead body, door");
            System.out.println("GO: door");

            if (bombKeyRevealed) {
                System.out.println("GET: flashlight, key, green key");
                System.out.println("DROP: flashlight, key, green key");
            } else {
                System.out.println("GET: flashlight, key");
                System.out.println("DROP: flashlight, key");
            }

            System.out.println("USE: none");
        } else if (room == redRoom) {
            System.out.println("LOOK: walls, floor, wooden door");
            System.out.println("GO: white, blue, cell, wooden door");
            System.out.println("GET: none");
            System.out.println("DROP: inventory items");
            System.out.println("USE: none");
        } else if (room == whiteRoom) {
            System.out.println("LOOK: sink, toilet, tank, bathtub");
            System.out.println("GO: red, back");
            System.out.println("GET: cup, magnet chain, drain key");
            System.out.println("DROP: cup, magnet chain, drain key");
            System.out.println("USE: sink, toilet, tank, tube, bathtub, drain");
        } else if (room == blueRoom) {
            System.out.println("LOOK: fish tank, chest");
            System.out.println("GO: red, back");
            System.out.println("GET: hammer, crank");
            System.out.println("DROP: hammer, crank");
            System.out.println("USE: chest, tank");
        } else if (room == greyRoom) {
            System.out.println("LOOK: hole, metal door, scanner");
            System.out.println("GO: metal door, back");
            System.out.println("GET: none");
            System.out.println("DROP: inventory items");
            System.out.println("USE: scanner");
        } else if (room == lobbyRoom) {
            System.out.println("LOOK: exit door, front desk, couches, chairs");
            System.out.println("GO: purple, wooden, exit door, back");
            System.out.println("GET: none");
            System.out.println("DROP: inventory items");
            System.out.println("USE: keycard");
        } else if (room == lionRoom) {
            System.out.println("LOOK: lion, cage, lion cage");
            System.out.println("GO: cage, back");
            System.out.println("GET: keycard");
            System.out.println("DROP: inventory items");
            System.out.println("USE: lion");
        } else if (room == supplyCloset) {
            System.out.println("LOOK: toolbox, knife");
            System.out.println("GO: back");
            System.out.println("GET: knife, pliers");
            System.out.println("DROP: knife, pliers");
            System.out.println("USE: none");
        } else if (room == wireRoom) {
            System.out.println("LOOK: table, wires, escape door");
            System.out.println("GO: escape door, back");
            System.out.println("GET: none");
            System.out.println("DROP: inventory items");
            System.out.println("USE: wires");
        } else if (room == escapeRoom) {
            System.out.println("LOOK: outside");
            System.out.println("GO: none");
            System.out.println("GET: none");
            System.out.println("DROP: none");
            System.out.println("USE: none");
        }

        System.out.println("COMMANDS: LOOK, GO, GET, DROP, INVENTORY, SAVE, RESTORE, QUIT");
        showInventoryHint();
    }

    private void showInventoryHint() {
        ArrayList<Item> inv = player.getCurrentInventory();
        System.out.print("INVENTORY: ");

        if (inv.isEmpty()) {
            System.out.println("empty");
            return;
        }

        for (int i = 0; i < inv.size(); i++) {
            System.out.print(inv.get(i).getName());
            if (i < inv.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    private void showInventory() {
        ArrayList<Item> inv = player.getCurrentInventory();

        if (inv.isEmpty()) {
            System.out.println("Your inventory is empty.");
            return;
        }

        System.out.println("You are carrying:");
        for (Item item : inv) {
            System.out.println("- " + item.getName());
        }
    }

    private boolean hasItem(String name) {
        for (Item item : player.getCurrentInventory()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private Item removeInventoryItem(String name) {
        ArrayList<Item> inv = player.getCurrentInventory();

        for (int i = 0; i < inv.size(); i++) {
            if (inv.get(i).getName().equalsIgnoreCase(name)) {
                return inv.remove(i);
            }
        }
        return null;
    }

    private Item findItemInInventory(String name) {
        for (Item item : player.getCurrentInventory()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    private void saveGame() {
        try (PrintWriter out = new PrintWriter(new FileWriter("save.txt"))) {
            out.println("room=" + player.getCurrentRoom().getName());
            out.println("keyRevealed=" + keyRevealed);
            out.println("flashlightDead=" + flashlightDead);
            out.println("toiletFlushed=" + toiletFlushed);
            out.println("tankOpened=" + tankOpened);
            out.println("cupFilled=" + cupFilled);
            out.println("magnetRevealed=" + magnetRevealed);
            out.println("bathtubChecked=" + bathtubChecked);
            out.println("drainKeyRevealed=" + drainKeyRevealed);
            out.println("chestOpened=" + chestOpened);
            out.println("blueFlooded=" + blueFlooded);
            out.println("crankTaken=" + crankTaken);
            out.println("greyDoorUnlocked=" + greyDoorUnlocked);
            out.println("knifeBroken=" + knifeBroken);
            out.println("toolboxOpened=" + toolboxOpened);
            out.println("keycardTaken=" + keycardTaken);
            out.println("bombStarted=" + bombStarted);
            out.println("bombKeyRevealed=" + bombKeyRevealed);

            long remaining = 0L;
            if (bombStarted) {
                remaining = bombEndTimeMillis - System.currentTimeMillis();
                if (remaining < 0) {
                    remaining = 0L;
                }
            }
            out.println("bombRemaining=" + remaining);

            StringBuilder inv = new StringBuilder();
            for (Item item : player.getCurrentInventory()) {
                inv.append(item.getName()).append(",");
            }
            out.println("inventory=" + inv);

            System.out.println("Game saved.");
        } catch (IOException e) {
            System.out.println("Could not save the game.");
        }
    }

    private void restoreGame() {
        setupWorld();

        try (BufferedReader in = new BufferedReader(new FileReader("save.txt"))) {
            String line;
            String roomName = "Dark Cell";
            String inventoryLine = "";
            long savedBombRemaining = 0L;

            while ((line = in.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length < 2) {
                    continue;
                }

                String key = parts[0];
                String value = parts[1];

                switch (key) {
                    case "room":
                        roomName = value;
                        break;
                    case "keyRevealed":
                        keyRevealed = Boolean.parseBoolean(value);
                        break;
                    case "flashlightDead":
                        flashlightDead = Boolean.parseBoolean(value);
                        break;
                    case "toiletFlushed":
                        toiletFlushed = Boolean.parseBoolean(value);
                        break;
                    case "tankOpened":
                        tankOpened = Boolean.parseBoolean(value);
                        break;
                    case "cupFilled":
                        cupFilled = Boolean.parseBoolean(value);
                        break;
                    case "magnetRevealed":
                        magnetRevealed = Boolean.parseBoolean(value);
                        break;
                    case "bathtubChecked":
                        bathtubChecked = Boolean.parseBoolean(value);
                        break;
                    case "drainKeyRevealed":
                        drainKeyRevealed = Boolean.parseBoolean(value);
                        break;
                    case "chestOpened":
                        chestOpened = Boolean.parseBoolean(value);
                        break;
                    case "blueFlooded":
                        blueFlooded = Boolean.parseBoolean(value);
                        break;
                    case "crankTaken":
                        crankTaken = Boolean.parseBoolean(value);
                        break;
                    case "greyDoorUnlocked":
                        greyDoorUnlocked = Boolean.parseBoolean(value);
                        break;
                    case "knifeBroken":
                        knifeBroken = Boolean.parseBoolean(value);
                        break;
                    case "toolboxOpened":
                        toolboxOpened = Boolean.parseBoolean(value);
                        break;
                    case "keycardTaken":
                        keycardTaken = Boolean.parseBoolean(value);
                        break;
                    case "bombStarted":
                        bombStarted = Boolean.parseBoolean(value);
                        break;
                    case "bombKeyRevealed":
                        bombKeyRevealed = Boolean.parseBoolean(value);
                        break;
                    case "bombRemaining":
                        savedBombRemaining = Long.parseLong(value);
                        break;
                    case "inventory":
                        inventoryLine = value;
                        break;
                }
            }

            clearRoomItems();
            rebuildWorldItems();

            if (!inventoryLine.isEmpty()) {
                String[] names = inventoryLine.split(",");
                for (String name : names) {
                    Item item = makeItemByName(name.trim());
                    if (item != null) {
                        player.getItem(item);
                    }
                }
            }

            player.setCurrentRoom(roomByName(roomName));

            if (bombStarted) {
                bombEndTimeMillis = System.currentTimeMillis() + savedBombRemaining;
            }

            running = true;
            deathChoiceActive = false;
            snakeQuestionActive = false;
            roomJustChanged = false;

            clearScreen();
            System.out.println("Game restored.");
            System.out.println();
            showRoomOptions();

        } catch (IOException e) {
            System.out.println("Could not restore the game.");
        }
    }

    private void clearRoomItems() {
        darkCell.getItem().clear();
        redRoom.getItem().clear();
        whiteRoom.getItem().clear();
        blueRoom.getItem().clear();
        coldDarkRoom.getItem().clear();
        greyRoom.getItem().clear();
        lobbyRoom.getItem().clear();
        lionRoom.getItem().clear();
        supplyCloset.getItem().clear();
        wireRoom.getItem().clear();
        escapeRoom.getItem().clear();
    }

    private void rebuildWorldItems() {
        if (!hasItem("flashlight")) {
            darkCell.addItem(flashlight);
        }

        darkCell.addItem(book);
        darkCell.addItem(deadBody);

        if (keyRevealed && !hasItem("key")) {
            darkCell.addItem(cellKey);
        }

        if (bombKeyRevealed && !hasItem("green key")) {
            darkCell.addItem(greenEscapeKey);
        }

        if (!hasItem("cup")) {
            whiteRoom.addItem(cup);
        }

        if (magnetRevealed && !hasItem("magnet chain")) {
            whiteRoom.addItem(magnetChain);
        }

        if (drainKeyRevealed && !hasItem("drain key")) {
            whiteRoom.addItem(drainKey);
        }

        if (!knifeBroken && !hasItem("knife")) {
            supplyCloset.addItem(knife);
        }

        if (toolboxOpened && !hasItem("pliers")) {
            supplyCloset.addItem(pliers);
        }

        if (chestOpened && !hasItem("hammer")) {
            blueRoom.addItem(hammer);
        }

        if (keycardTaken && !hasItem("keycard")) {
            lionRoom.addItem(keycard);
        }
    }

    private Room roomByName(String name) {
        if (name.equalsIgnoreCase("Dark Cell")) return darkCell;
        if (name.equalsIgnoreCase("Red Padded Room")) return redRoom;
        if (name.equalsIgnoreCase("White Bathroom")) return whiteRoom;
        if (name.equalsIgnoreCase("Blue Room")) return blueRoom;
        if (name.equalsIgnoreCase("Cold Dark Room")) return coldDarkRoom;
        if (name.equalsIgnoreCase("Grey Room")) return greyRoom;
        if (name.equalsIgnoreCase("Lobby")) return lobbyRoom;
        if (name.equalsIgnoreCase("Lion Room")) return lionRoom;
        if (name.equalsIgnoreCase("Supply Closet")) return supplyCloset;
        if (name.equalsIgnoreCase("Wire Room")) return wireRoom;
        if (name.equalsIgnoreCase("Outside")) return escapeRoom;
        return darkCell;
    }

    private Item makeItemByName(String name) {
        if (name.equalsIgnoreCase("flashlight")) return new Item("flashlight", "A small flashlight. It still works.", "tool");
        if (name.equalsIgnoreCase("key")) return new Item("key", "A small metal key hidden inside the book.", "key");
        if (name.equalsIgnoreCase("cup")) return new Item("cup", "A plain cup from the sink.", "tool");
        if (name.equalsIgnoreCase("magnet chain")) return new Item("magnet chain", "A chain with a magnet attached.", "tool");
        if (name.equalsIgnoreCase("drain key")) return new Item("drain key", "A key pulled from the bathtub drain.", "key");
        if (name.equalsIgnoreCase("hammer")) return new Item("hammer", "A sturdy hammer.", "tool");
        if (name.equalsIgnoreCase("knife")) return new Item("knife", "A sharp knife from the wall.", "weapon");
        if (name.equalsIgnoreCase("keycard")) return new Item("keycard", "A keycard taken from the dead body behind the lion.", "keycard");
        if (name.equalsIgnoreCase("pliers")) return new Item("pliers", "A pair of pliers from the toolbox.", "tool");
        if (name.equalsIgnoreCase("green key")) return new Item("green key", "A green key taken from the dead body.", "key");
        if (name.equalsIgnoreCase("crank")) return new Item("crank", "A crank handle from the fish tank.", "tool");
        return null;
    }
}