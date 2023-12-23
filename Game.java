

import java.util.HashMap;

public class Game {
    private Parser parser;
    private Room currentRoom;
    private HashMap<String, Item> inventory;

    public Game() {
        createRooms();
        parser = new Parser();
        inventory = new HashMap<>();
    }

    private void createRooms() {
        Room entrance, SpawnArea, Pathway, Trap, ChestRoom, BossBattle, SpecialItem, SecretPath, Exit;
    
        entrance = new Room("The entrance to the spooky dungeon", false);
        SpawnArea = new Room("in spawn area, path is south", false);
        Pathway = new Room("in the pathway, three routes, west, east, or south", false);
        Trap = new Room("in the trap room, you have either pathway east or special item west", false);
        ChestRoom = new Room("in the Chest room, go back west to pathway", false);
        BossBattle = new Room("in the south boss room, south is exit, north is path", false);
        SpecialItem = new Room("You in special item room. Further east in the trap room, south to secret path", false);
        SecretPath = new Room("you in secret path, east to exit, or north to go back special item", false);
        Exit = new Room("looking toward the south entrance to the world, west is to go back to secret path, or north boss room", true);
    
        entrance.setExit("upstairs", entrance);
        entrance.setExit("downstairs", SpawnArea);
    
        SpawnArea.setExits(null, null, Pathway, null);
    
        Pathway.setExits(SpawnArea, ChestRoom, BossBattle, Trap);
        Trap.setExits(null, Pathway, null, SpecialItem);
        SpecialItem.setExits(null, Trap, SecretPath, null);
        SecretPath.setExits(SpecialItem, Exit, null, null);
        BossBattle.setExits(Pathway, null, Exit, null);
        ChestRoom.setExits(null, null, null, Pathway);
    
        ChestRoom.addItem(new Item("Sword", "A sharp sword for battle"));
        SpecialItem.addItem(new Item("Shield", "A sturdy shield for defense"));
    
        currentRoom = entrance; // start the game outside
    }
    
    public void play() {
        printWelcome();

        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing. Goodbye.");
    }

    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println("You are " + currentRoom.getDescription());
        System.out.print("You can go: ");
        System.out.println(currentRoom.getExitString());
        System.out.println(currentRoom.getItemsString());
    }

    private void printLocationInfo() {
        System.out.println("You are" + currentRoom.getDescription());
        System.out.print("You can go:");
        System.out.print(currentRoom.getExitString());
        System.out.println();
        System.out.println(currentRoom.getItemsString());
    }

   
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;
    
        if (command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }
    
        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        } else if (commandWord.equals("go")) {
            goRoom(command);
        } else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        } else if (commandWord.equals("take")) {
            takeItem(command);
        } else {
            System.out.println("Invalid command.");
        }
    
        return wantToQuit;
    }
    

    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println("   go quit help take");
    }

    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }
    
        String direction = command.getSecondWord();
    
        // Try to leave the current room.
        Room nextRoom = currentRoom.getExit(direction);
    
        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else {
            currentRoom = nextRoom;
            System.out.println("You are " + currentRoom.getDescription());
            System.out.print("Exits: ");
            printLocationInfo();
    
            // Check if the player has reached the exit room (goal).
            if (currentRoom.isExitRoom()) {
                System.out.println("Congratulations! You have reached the exit room. Game over!");
                System.exit(0); // Terminate the program
            }
        }
    }
    
    private void takeItem(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Take what?");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = currentRoom.takeItem(itemName);

        if (item != null) {
            inventory.put(itemName, item);
            System.out.println("You have taken: " + itemName);
        } else {
            System.out.println("There is no such item here.");
        }
    }

    private boolean quit(Command command) {
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } else {
            return true;
        }
    }
}

