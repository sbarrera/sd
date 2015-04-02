package edu.ub.sd.sevenhalf.player;

import edu.ub.sd.sevenhalf.player.menu.Menu;
import edu.ub.sd.sevenhalf.player.menu.MenuOption;
import edu.ub.sd.sevenhalf.runtime.GameExecutable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainMenu extends GameExecutable {

    private boolean menuEnabled = true;
    private WrappedMainMenu mainMenu;

    public MainMenu(String[] args) throws Exception {
        super(args);

        if ( parameters.doShowHelp() ) {
            showHelp();
            menuEnabled = false;
        } else if ( parameters.getTopCard() > 0.0 ) {
            BotClient client = new BotClient(parameters);
            client.run();
            menuEnabled = false;
        } else {
            mainMenu = new WrappedMainMenu();
        }
    }

    @Override
    public void run() {
        mainMenu.run();
    }

    public boolean isMenuEnabled(){
        return menuEnabled;
    }

    @Override
    public void showHelp() {
        System.out.println("java Client -s <maquina_servidora> -p <port> [-a topcard]");
    }

    @Override
    public List<String> getRequiredArguments() {
        return Arrays.asList(new String[]{ "-s", "-p" });
    }

    @Override
    public List<String> getOptionalArguments() {
        return Arrays.asList(new String[]{ "-a" });
    }

    class WrappedMainMenu extends Menu {

        private GameMenu gameMenu;

        @Override
        public void showHeader() {
            System.out.println("            Seven and Half");
        }

        @Override
        public List<MenuOption> getOptions() {
            List<MenuOption> options = new ArrayList<>();

            options.add(new MenuOption(1, "Play Seven and Half"){
                public void onSelect() {
                    gameMenu = new GameMenu(parameters);
                    gameMenu.run();
                }
            });

            options.add(new MenuOption(2, "Quit"){
                public void onSelect() {
                    WrappedMainMenu.this.close();
                }
            });

            return options;
        }

    }

    public static void main(String args[]) {
        try {
            MainMenu menu = new MainMenu(args);
            if ( menu.isMenuEnabled() ) {
                menu.run();
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }

}
