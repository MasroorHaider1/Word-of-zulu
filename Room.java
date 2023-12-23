

import java.util.Set;
import java.util.HashMap;

public class Room {
    public String description;
    private HashMap<String, Room> exits;
    private HashMap<String, Item> items;
    private boolean isExitRoom;

    public Room(String description, boolean isExitRoom) {
        this.description = description;
        exits = new HashMap<>();
        items = new HashMap<>();
        this.isExitRoom = isExitRoom;
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    public void setExits(Room north, Room east, Room south, Room west) {
        if (north != null) {
            exits.put("north", north);
        }
        if (east != null) {
            exits.put("east", east);
        }
        if (south != null) {
            exits.put("south", south);
        }
        if (west != null) {
            exits.put("west", west);
        }
    }

    public void addItem(Item item) {
        items.put(item.getName(), item);
    }

    public Item takeItem(String itemName) {
        return items.remove(itemName);
    }

    public String getItemsString() {
        if (items.isEmpty()) {
            return "No items in this room.";
        }

        StringBuilder itemsString = new StringBuilder("Items in this room:");
        for (String itemName : items.keySet()) {
            itemsString.append(" ").append(itemName);
        }
        return itemsString.toString();
    }

    public boolean isExitRoom() {
        return isExitRoom;
    }

    public String getDescription() {
        return description;
    }

    public String getExitString() {
        StringBuilder returnString = new StringBuilder("Exits:");
        for (String exit : exits.keySet()) {
            returnString.append(" ").append(exit);
        }
        return returnString.toString();
    }
}
