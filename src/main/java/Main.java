import java.io.*;

public class Main {

    public static Replay[] getReplaysFromFiles(String[] filenames) {
        Replay[] replays = new Replay[filenames.length];
        for (int i = 0; i < replays.length; i++) {
            try {
                replays[i] = new Replay(filenames[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return replays;
    }

    public static void generateFile(Replay[] replays) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File("output.csv"));

        int maxPlayers = Integer.MIN_VALUE;
        for(Replay replay: replays) {
            if (replay != null) {
                if (replay.getPlayerCount() > maxPlayers) maxPlayers = replay.getPlayerCount();
            }
        }

        StringBuilder players = new StringBuilder();
        for(int i = 0; i < maxPlayers; i++) {
            int ii = i;
            players.append(String.format(" Player %d Table Position, Player %d Name, Player %d DeckHash Hash, Player %d Hands Drawn", ii,
                    ii, ii, ii));
            if (i < maxPlayers - 2) players.append(",");
        }

        writer.printf("Replay ID, Player Count, Turns Taken, Duration, Spectators Allowed, " +
                "Spectators Can Chat, Spectators Can see Hand,%s\n", players.toString());

        for (int i = 0; i < replays.length; i++) {
            if (replays[i] != null) {
                writer.println(replays[i].getCSVLine());
            }
        }

        writer.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            System.out.println("Help: \n" +
                    "    --replay-folder <replayfolderpath/>\n" +
                    "    --replays <replay1.cor> <replay2.cor>\n" +
                    "The output file is output.csv");
            throw new IllegalArgumentException("No arguments, help shown above.");
        } else if (args.length == 1) {
            throw new IllegalArgumentException("Please add arguments.");
        } else if (args[0].equals("--replays")){
            String[] replays = new String[args.length - 1];
            System.arraycopy(args, 1, replays, 0, args.length - 1);
            generateFile(getReplaysFromFiles(replays));
        } else if (args[0].equals("--replay-folder")){
            File f = new File(args[1]);
            if (f.isDirectory()) {
                File[] files = f.listFiles();
                assert files != null;
                String[] replays = new String[files.length];

                for (int i = 0; i < files.length; i++) {
                    replays[i] = files[i].getAbsolutePath();
                }

                generateFile(getReplaysFromFiles(replays));
            } else {
                System.out.println("Error got file, expecting folder.");
            }
        } else {
            System.out.println("Help: \n" +
                    "    --replay-folder <replayfolderpath/>\n" +
                    "    --replays <replay1.cor> <replay2.cor>\n" +
                    "The output file is output.csv");
            throw new IllegalArgumentException("No valid arguments, help shown above.");
        }
    }

}
