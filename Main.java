import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;


public class Main {
    public static class Item {
        String name;
        int weight;
    }

    public interface Spaceship {
        //launch & land success = true, crash = false
        boolean launch(double random);
        boolean land(double random);
        //check if Rocket can carry more item
        boolean canCarry(Item item);
        void carry(Item item);
    }

    public static class Rocket implements Spaceship {
        int currentWeight, launchCost;
        int maxWeight;
        // launch and land will be override in class U1, U2
        public boolean launch(double random) {
            return true;
        }
        public boolean land(double random) {
            return true;
        }
        public boolean canCarry(Item item) {
            return (maxWeight >= (currentWeight + item.weight));
        }
        public void carry(Item item) {
            if (canCarry(item)) {
                currentWeight += item.weight;
            }
        }

    }
    // U1 is child type 1 of rocket
    public static class U1 extends Rocket {
        public void initialValue() {
            maxWeight = 18000;
            currentWeight = 10000;
            launchCost = 100000;
        }
        public boolean launch(double random) {
            return (random > (currentWeight / maxWeight) * 5 / 100);
        }
        public boolean land(double random) {
            return (random > (currentWeight / maxWeight) * 1 / 100);
        }
    }
    // U2 is child type 1 of rocket
    public static class U2 extends Rocket {
        public void initialValue() {
            maxWeight = 29000;
            currentWeight = 18000;
            launchCost = 120000;
        }
        public boolean launch(double random) {
            return (random > (currentWeight / maxWeight) * 4 / 100);
        }
        public boolean land(double random) {
            return (random > (currentWeight / maxWeight) * 8 / 100);
        }
    }

    public static class Simulation {
        // This method scan file for List of Item will be loaded
        ArrayList <Item> loadItems(File phase) {
            ArrayList <Item> list = new ArrayList();
            Scanner scanner = null;
            try {
                scanner = new Scanner(phase);
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                //e.printStackTrace();
            }
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //split line by delimeter "=" into 2 part, part 1 is item name, part 2 is item weight
                String splitInput[] = line.split("=");
                Item item = new Item();
                item.name = splitInput[0];
                item.weight = Integer.parseInt(splitInput[1]);
                //push item to Arraylist
                list.add(item);
            }
            return list;
        }
        ArrayList <U1> loadU1(File phase) {
            //List of items will be load (input)
            ArrayList<Item> listItems = loadItems(phase);
            //List of U1 rocket launched
            ArrayList<U1> launchedU1 = new ArrayList();

            while (!listItems.isEmpty()) {
                U1 rocket = new U1();
                rocket.initialValue();
                int i = 0;
                while (rocket.canCarry(listItems.get(i))) {
                    rocket.carry(listItems.get(i));
                    //if no item left in the list, break
                    if (listItems.size() == i+1){
                        i++;
                        break;
                    }
                    i++;
                }
                // if rocket successfully launch or land, delete items loaded, else build this rocket and load again
                if (rocket.launch((Math.random())) && rocket.land(Math.random())) {
                    while (i>0) {
                        listItems.remove(i-1);
                        i--;
                        if (listItems.isEmpty()){
                            break;
                        }
                    }
                }
                launchedU1.add(rocket);
                System.out.println("number of rocket U1:" + launchedU1.size());
            }
            return launchedU1;
        }
        ArrayList <U2> loadU2(File phase) {
            //List of items will be load (input)
            ArrayList<Item> listItems = loadItems(phase);
            //List of U2 rocket launched
            ArrayList<U2> launchedU2 = new ArrayList();
            while (!listItems.isEmpty()) {
                U2 rocket = new U2();
                rocket.initialValue();
                int i = 0;
                while (rocket.canCarry(listItems.get(i))) {
                    rocket.carry(listItems.get(i));
                    //if no item left in the list, break
                    if (listItems.size() == i+1){
                        i++;
                        break;
                    }
                    i++;
                }
                // if rocket successfully launch or land, delete items loaded, else build this rocket and load again
                if (rocket.launch((Math.random())) && rocket.land(Math.random())) {
                    while (i>0) {
                        listItems.remove(i-1);
                        i--;
                        if (listItems.isEmpty()){
                            break;
                        }
                    }
                }
                launchedU2.add(rocket);
                System.out.println("number of rocket U2:" + launchedU2.size());
            }
            return launchedU2;
        }

        int fullTest(File phase1, File phase2){
            U1 u1 = new U1();
            U2 u2 = new U2();
            u1.initialValue();
            u2.initialValue();
            ArrayList<U1> launchedU1 = loadU1(phase1);
            ArrayList<U2> launchedU2 = loadU2(phase2);
            return (launchedU1.size()*u1.launchCost + launchedU2.size()*u2.launchCost);
        }
    }

    public static void main(String[] args) {
        //loading phases txt file
        File phase1 = new File("phase-1.txt");
        File phase2 = new File("phase-2.txt");

        Simulation run = new Simulation();
        //run simulation
        int budget = run.fullTest(phase1,phase2);

        System.out.println("Budget needed for all operation: $"+budget);
    }
}
