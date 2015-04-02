package edu.ub.sd.sevenhalf.player.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public abstract class Menu implements Runnable {

    protected boolean close = false;

    public abstract void showHeader();
    public abstract List<MenuOption> getOptions();

    public void run() {
        Integer selectedOption = null;
        List<MenuOption> options = getOptions();
        close = false;

        if ( options.size() > 0 ) {
            Map<Integer, MenuOption> callbacks = getMapOptions(options);

            while ( !isClosed() ) {
                System.out.println();

                printSeparator();
                showHeader();
                printSeparator();

                showOptions(options);
                selectedOption = readInteger("Select an option: ");

                if ( callbacks.containsKey(selectedOption) ) {
                    callbacks.get(selectedOption).onSelect();
                } else {
                    System.out.println("WARNING: Unknown option.");
                }

                System.out.println();
            }
        } else {
            printSeparator();
            showHeader();
            printSeparator();
        }
    }

    public void close() {
        close = true;
    }

    public boolean isClosed() {
        return close;
    }

    protected void showOptions(List<MenuOption> options) {
        for ( MenuOption menuOption : options) {
            System.out.println(String.format("%2s. %s", menuOption.getId(), menuOption.getDescription()));
        }
    }

    protected Map<Integer, MenuOption> getMapOptions(List<MenuOption> options) {
        Map<Integer, MenuOption> map = new HashMap<>();

        for ( MenuOption menuOption : options) {
            map.put(menuOption.getId(), menuOption);
        }

        return map;
    }

    protected int readInteger(String message) {
        Scanner sc = new Scanner(System.in);
        System.out.print(message);
        while ( !sc.hasNextInt() )
        {
            System.out.println("WARNING: Input is not an integer.");
            System.out.print(message);
            sc.next();
        }

        return sc.nextInt();
    }

    protected void printSeparator() {
        System.out.println("----------------------------------------");
    }

}
