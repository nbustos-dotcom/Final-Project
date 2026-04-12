//Author: Marilyn Stephen
/**
 * Item represents an object the player can pick up, carry, or use.
 * This class stores item data only and contains no game or puzzle logic.
 * 
 * Used by: Player (inventory), Room (room items), Game (puzzle checks)
 */


public class Item {
	// Fields
	private String name; // Display name used in commands like GET, DROP, INVENTORY
	private String description; // Shown when the player inspects or sees the item
	private String type; // Logical category used for puzzle checks (key, weapon, tool, etc.)

	  
	/**
	 * Creates a new item with a name, description, and type.
	 * 
	 * @param name Item name used in commands
	 * @param description Text shown to the player
	 * @param type Used by Game.java for puzzle logic
	 */
	public Item(String name, String description, String type) {
		this.name = name;
		this.description = description;
		this.type = type;
		}
	


	/**
	 * Returns the item name for comparison and display purposes.
	 * Used when picking up, dropping, or listing items.
	 */
	public String getName() {
		return name;
		}


	/**
	 * Returns the description shown to the player.
	 */
	public String getDescription() {
		return description;
		}

    
	/**
	 * Returns the item type used in puzzle and challenge checks.
	 */		
	public String getType() {
		return type;
		}
	
	}

