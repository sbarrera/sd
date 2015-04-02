package edu.ub.sd.sevenhalf.player;

import edu.ub.sd.sevenhalf.player.menu.Menu;
import edu.ub.sd.sevenhalf.player.menu.MenuOption;
import edu.ub.sd.sevenhalf.runtime.GameParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergi on 23/02/2015.
 */
public class GameMenu extends Menu {

    private GameParameters parameters;
    private GameClient client;
    private boolean endGame;

    public GameMenu(GameParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public void run() {
        try {
            client = new GameClient(parameters);

            while ( !hasGameEnded() ) {
                client.run();

                if ( client.isWaitingForPlayerInput() || client.hasGameEnded() ) {
                    super.run();
                }

                if ( client.hasErrorRisen() || client.hasGameEnded() ) {
                    endGame();
                }
            }
        } catch ( Exception ex ) {
            endGame();
        } finally {
            if ( client != null ) {
                client.shutdown();
            }
        }
    }

    @Override
    public void showHeader() {
        if ( !client.hasErrorRisen() && !hasGameEnded() ) {
            System.out.println(String.format("  Current Bet: [%s]", client.getCurrentBet()));
            System.out.println(String.format("  Drawn Cards: [%s]", client.getHand()));
        }

        if ( client.hasGameBusted() ) {
            System.out.println("  BUSTED!");
        }

        if ( client.hasGameEnded() ) {
            System.out.println(String.format("  Bank Drawn Cards: [%s]", client.getBankHand()));
            System.out.println(String.format("  Gains: [%s]", client.getGains()));
        }
    }

    @Override
    public List<MenuOption> getOptions() {
        List<MenuOption> options = new ArrayList<>();
        int optionId = 0;

        if ( !client.hasGameEnded() ) {
            options.add(new MenuOption(++optionId, "DRAW") {
                public void onSelect() {
                    client.drawCard();
                    GameMenu.this.close();
                }
            });

            if (client.getCurrentScore() > 0.0) {
                //Ante implica recibir carta, tras dar el incremento en la apuesta
                options.add(new MenuOption(++optionId, "ANTE") {
                    public void onSelect() {
                        int additionalBet = readInteger("Please, state how much do you want to bet? ");
                        client.anteUp(additionalBet);
                        GameMenu.this.close();
                    }
                });

                options.add(new MenuOption(++optionId, "PASS") {
                    public void onSelect() {
                        client.pass();
                        GameMenu.this.close();
                    }
                });
            }

            options.add(new MenuOption(++optionId, "Quit") {
                public void onSelect() {
                    GameMenu.this.endGame();
                    GameMenu.this.close();
                }
            });
        }
        return options;
    }

    public void endGame() {
        endGame = true;
    }

    public boolean hasGameEnded() {
        return endGame;
    }
}
