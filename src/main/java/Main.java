import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static Replay getReplaysFromFile(String replay) throws IOException {
        return new Replay(replay);
    }

    public static void generateOutput(Replay replay) {
        List<Player> players = new LinkedList<>();
        AtomicInteger spectators = new AtomicInteger();

        for (Player player: replay.getPlayers()) {
            if (player.isSpectator(4)) {
                spectators.getAndIncrement();
            } else {
                players.add(player);
            }
        }

        System.out.printf("spectators:%d\n", spectators.get());
        for (Player player: players) {
            System.out.printf("%s %s\n", player.getName(), player.getDeckHash());
        }

        System.out.printf("turns:%d", replay.getTurnsTaken());
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 1) {
            try {
                generateOutput(getReplaysFromFile(args[0]));
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(13);
            }
        } else {
            System.out.println("Usage java -jar replaytool.jar [replay.cor]");
            System.exit(13);
        }
    }

}
