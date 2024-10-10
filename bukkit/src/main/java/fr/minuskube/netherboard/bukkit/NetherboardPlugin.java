package fr.minuskube.netherboard.bukkit;

import fr.minuskube.netherboard.Netherboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class NetherboardPlugin extends JavaPlugin implements Listener {

    private final Map<Player, String> currentBoards = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(player, player.getName() + " rejoined."));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                switchBoards();
            }
        }.runTaskTimer(this, 0, 100); // 100 ticks = 5 seconds
    }

    @Override
    public void onDisable() {
        Netherboard.instance().getAllBoards().values().forEach(m -> m.values().forEach(BPlayerBoard::delete));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Create multiple boards
        Netherboard.instance().createBoard(player, "Board1");
        Netherboard.instance().createBoard(player, "Board2");

        // Initialize current board
        currentBoards.put(player, "Board1");

        BPlayerBoard board2 = Netherboard.instance().getBoard(player, "Board2");
        board2.set("Line 2", 1);

        // Use the initial board
        BPlayerBoard board1 = Netherboard.instance().getBoard(player, "Board1");
        board1.set("Line 1", 1);

        player.sendMessage("Created board.");
    }

    private void switchBoards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String currentBoard = currentBoards.get(player);
            String nextBoard = currentBoard.equals("Board1") ? "Board2" : "Board1";
            currentBoards.put(player, nextBoard);

            BPlayerBoard board = Netherboard.instance().getBoard(player, nextBoard);
            if (board != null) {
                player.setScoreboard(board.getScoreboard());
                player.sendMessage("SWITCH BOARD : " + board.getName());
            }
        }
    }

}
