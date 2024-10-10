package fr.minuskube.netherboard;

import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

/**
 * The main class of the Netherboard API for Bukkit,
 * you'll need to use it if you want to create boards.
 *
 * To create a board, get the instance using <code>Netherboard.instance()</code>
 * and call one of the <code>createBoard()</code> methods.
 */
public class Netherboard {

    private static Netherboard instance;

    private final Map<Player, Map<String, BPlayerBoard>> boards = new HashMap<>();

    private Netherboard() {}

    /**
     * Creates a board for a player.
     *
     * @param player    the player
     * @param name      the name of the board
     * @return          the newly created board
     */
    public BPlayerBoard createBoard(Player player, String name) {
        return createBoard(player, null, name);
    }

    /**
     * Creates a board for a player, using a predefined scoreboard.
     *
     * @param player        the player
     * @param scoreboard    the scoreboard to use
     * @param name          the name of the board
     * @return              the newly created board
     */
    public BPlayerBoard createBoard(Player player, Scoreboard scoreboard, String name) {
        Map<String, BPlayerBoard> playerBoards = boards.computeIfAbsent(player, k -> new HashMap<>());
        deleteBoard(player, name);

        BPlayerBoard board = new BPlayerBoard(player, scoreboard, name);
        playerBoards.put(name, board);
        return board;
    }

    /**
     * Deletes a specific board of a player.
     *
     * @param player the player
     * @param name   the name of the board
     */
    public void deleteBoard(Player player, String name) {
        Map<String, BPlayerBoard> playerBoards = boards.get(player);
        if (playerBoards != null && playerBoards.containsKey(name)) {
            playerBoards.get(name).delete();
            playerBoards.remove(name);
        }
    }

    /**
     * Deletes all boards of a player.
     *
     * @param player the player
     */
    public void deleteAllBoards(Player player) {
        Map<String, BPlayerBoard> playerBoards = boards.get(player);
        if (playerBoards != null) {
            playerBoards.values().forEach(BPlayerBoard::delete);
            playerBoards.clear();
        }
    }

    /**
     * Gets a specific board of a player.
     *
     * @param player the player
     * @param name   the name of the board
     * @return       the player board, or null if the player has no board with the given name
     */
    public BPlayerBoard getBoard(Player player, String name) {
        Map<String, BPlayerBoard> playerBoards = boards.get(player);
        return playerBoards != null ? playerBoards.get(name) : null;
    }

    /**
     * Gets all the boards of a player.
     *
     * @param player the player
     * @return       a map of board names to player boards
     */
    public Map<String, BPlayerBoard> getBoards(Player player) {
        return boards.getOrDefault(player, new HashMap<>());
    }

    public Map<Player, Map<String, BPlayerBoard>> getAllBoards() {
        return new HashMap<>(boards);
    }

    /**
     * Returns the instance of the Netherboard class.
     *
     * @return the instance
     */
    public static Netherboard instance() {
        if(instance == null)
            instance = new Netherboard();

        return instance;
    }

}