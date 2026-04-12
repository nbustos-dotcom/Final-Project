import java.util.ArrayList;
import java.util.List;

//Author: Marilyn Stephen
/**
 * Room represents a location in the game.
 * A room contains a description, items, and exits to other rooms.
 * 
 * This class stores room data only and does not control game logic.
 */

public class Room {
	
	
	//Fields
	private String name; 
	private String description;  
	
	

	// Parallel lists to store exits
	// directions.get(i) corresponds to connectedRooms.get(i)
	private List<String> directions;
	private List<Room> connectedRooms;
	
	
	private List<Item> items;
	
	
	
	//Constructor 	
	public Room(String name, String description) {
		this.name = name;
		this.description = description;
		
		this.directions = new ArrayList<>();
		this.connectedRooms = new ArrayList<>();
		this.items = new ArrayList<>();
	}
	
	
	
	// Exit Management	
	
	
	/**
	 * Connects this room to another room in a given direction.
	 * Direction should be lowercase (ex: "north", "south").
	 */
	public void addExit(String direction, Room room) {
		directions.add(direction.toLowerCase());
		connectedRooms.add(room);
	}
	

	/**
	 * Returns the room in the given direction, or null if no exit exists.
	 * Used by the GO command in Game.java.
	 */	
	public Room getExit(String direction) {
    direction = direction.toLowerCase();
    for (int i = 0; i < directions.size(); i++) {
			if (directions.get(i).equals(direction)) {
				return connectedRooms.get(i);
			}
		}
		return null;
	}
	
	public List<String> getExitDirections() {
		return directions;
	}
	
	
	
	// Item Management 
	

	/**
	 * Adds an item to this room.
	 * Typically called during world setup or when a player drops an item.
	 */	
	public void addItem(Item item) {
		items.add(item);
	}
	

	/**
	 * Removes and returns an item by name.
	 * Returns null if the item is not found.
	 * Used by the GET command.
	 */
	
	public Item removeitem(String itemName) {
		for(Item item : items) {
			if (item.getName().equalsIgnoreCase(itemName)) {
				items.remove(item);
				return item;
			}
		}
		return null;
	}
	
	public List<Item> getItem() {
		return items;
	}
	
	
	
	//Room Info
	public String getName() {
		return name;
	}
	

	/**
	 * Builds and returns the full room description including:
	 * - Room name
	 * - Base description
	 * - Items in the room
	 * - Available exits
	 * 
	 * Called when the player uses LOOK or enters a room.
	 */	
	public String getDescription() {
		StringBuilder output = new StringBuilder();
		
		output.append(name).append("\n");
		output.append(description).append("\n");
		
		if (!items.isEmpty()) {
			output.append("Items here: ");
			for(Item item : items) {
				output.append(item.getName()).append(" ");
			}
			output.append("\n");
		}
		
		output.append("Exits: ");
		for (String dir : directions) {
			output.append(dir).append(" ");
		}
		
		return output.toString();
		
	}
	
}

	

