# Final-Project
Text adventure game for final project

Person A – TextAdventure.java (Main Class / Game Starter)

Purpose: Start the game and connect all objects.

Import other classes (Player, Room, Item, Game).
Instantiate rooms and items (you can call constructors from Person D’s classes).
Instantiate the player and set their starting room.
Instantiate Game and pass the player and rooms to it.
Create a loop to take input commands from the user.
Forward commands to the Game class to handle logic.
Display text output returned from Game (like room descriptions, puzzle results, win/loss messages).

Deliverables:

A running main() method.
Basic text input/output loop.
Connects all classes.




Person B – Game.java (Game Logic / Challenges)

Purpose: Handle everything the player can do in the game.

Step-by-step tasks:

Store game state variables: current room, solved puzzles, bomb status, monster status.
Implement commands:
GO [direction] → move player to the correct room.
LOOK → return the room description from Room class.
INVENTORY → show player inventory from Player class.
GET [item] / DROP [item] → call Player methods.
SAVE / RESTORE → call Player methods for file I/O.
QUIT → end the game loop.
Implement puzzle logic:
Dark room key search with flashlight.
Color mixing puzzle.
Monster challenge (check knife in inventory).
Bomb puzzle (check pliers and wire color).
Implement win/loss detection.
Keep track of path choices for demo paths.

Deliverables:

Game command parser.
Puzzle/event logic.
Win/loss management.





Person C – Player.java (Player State / Inventory)

Purpose: Track the player and allow interactions with items and rooms.

Step-by-step tasks:

Store player state:
Current room (Room object).
Inventory (list of Item objects).
Methods to manipulate inventory:
getItem(Item item) → pick up an item.
dropItem(Item item) → drop an item in current room.
hasItem(String itemName) → check for specific items.
Methods to move between rooms:
setCurrentRoom(Room room) / getCurrentRoom().
Save/Restore:
Save current room and inventory to a file.
Restore from file to resume game.
Optional: track points or stats if doing extra credit.

Deliverables:

Fully functional player object.
Inventory management.
Movement between rooms.
Save/Restore.




Person D – Room.java & Item.java (Rooms and Objects)

Purpose: Define rooms and items, handle objects inside rooms.

Room.java tasks:

Store room info:
Name, description, exits (map of directions → Room), items in room (list of Item).
Methods:
addItem(Item item) / removeItem(Item item)
getDescription() → return room description with items/exits.
getExits() → return available directions.
Optional: trigger events in the room (like monster presence or bomb).

Item.java tasks:

Store item info:
Name, description, type (key, knife, pliers, etc.).
Optional: special properties (like bomb wire color, puzzle relevance).
Methods:
getName() / getDescription()

Deliverables:

Fully functional rooms with exits and items.
Items that can be picked up, used, or dropped.
