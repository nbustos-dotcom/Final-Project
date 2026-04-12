// Brooklyn Carlton -- working rn I promise!!!
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;

public class Player {
    Room currentRoom = null;
    ArrayList<Item> inventory = new ArrayList<Item>();

    public void getItem(Item newItem) {
        inventory.add(newItem);
    }

    public Item dropItem(Item oldItem) {
        inventory.remove(oldItem);
        return oldItem;
    }

    public boolean hasItem(Item item) {
        boolean hasItem = inventory.contains(item);
        return hasItem;
    }

    public void setCurrentRoom(Room newRoom) {
        currentRoom = newRoom;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void save() {
        try {
            FileWriter writer = new FileWriter("save.txt");
            writer.write(currentRoom.getName());
            writer.write(",");
            
            for (Item currentItem : inventory) {
                writer.write(currentItem.getName());
                writer.write(",");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            FileReader reader = new FileReader("save.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
