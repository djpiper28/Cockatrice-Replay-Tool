import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.InvalidProtocolBufferException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class Replay {

    private List<Player> players;

    class Player {
        @Override
        public String toString() {
            return "Player{" +
                    "deckHash='" + deckHash + '\'' +
                    ", name='" + name + '\'' +
                    ", id=" + id +
                    '}';
        }

        public String deckHash, name;
        public int id;

        public Player(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getCSV() {
            return String.format("%d,%s,%s", id, name, deckHash);
        }
    }

    private long replayID;
    private int playerCount;
    private int turnsTaken;
    private int duration;
    private boolean spectatorsAllowed, spectatorsCanChat, spectatorsCanSeeHand;

    public String getCSVLine() {
        return String.format("%d,%d,%d,%d,%b,%b,%b,%s",replayID,
                playerCount, turnsTaken, duration, spectatorsAllowed, spectatorsCanChat,
                spectatorsCanSeeHand, getPlayersCSV());
    }

    private String getPlayersCSV() {
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < players.size(); i++) {
            int ii = i;
            output.append(players.get(i).getCSV());

            if (i < players.size() - 2) output.append(",");
        }
        return output.toString();
    }

    public Replay(String filename) throws IOException {
        //Read replayFile into byte buffer
        this(Files.readAllBytes(Path.of(filename)));
    }

    public Replay(InputStream stream) throws IOException {
        //Read replayFile into byte buffer
        this(stream.readAllBytes());
    }

    public Replay(byte[] bytes) throws InvalidProtocolBufferException {
        ExtensionRegistry registry = ExtensionRegistry.newInstance();

        EventJoin.registerAllExtensions(registry);
        //EventLeave.registerAllExtensions(registry);
        EventSetActivePlayer.registerAllExtensions(registry);

        ContextDeckSelect.registerAllExtensions(registry);
        //ContextConcede.registerAllExtensions(registry);
        //CommandDeckSelect.registerAllExtensions(registry);

        //Parse byte buffer into replay
        GameReplayOuterClass.GameReplay replay = GameReplayOuterClass.GameReplay.parseFrom(bytes, registry);

        //Get the stats
        this.getStats(replay);
    }

    @Override
    public String toString() {
        return "Replay{" +
                "players=" + players.toString() +
                ", replayID=" + replayID +
                ", playerCount=" + playerCount +
                ", turnsTaken=" + turnsTaken +
                ", duration=" + duration +
                ", spectatorsAllowed=" + spectatorsAllowed +
                ", spectatorsCanChat=" + spectatorsCanChat +
                ", spectatorsCanSeeHand=" + spectatorsCanSeeHand +
                '}';
    }

    public List<Player> getPlayers() {
        return players;
    }

    public long getReplayID() {
        return replayID;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getTurnsTaken() {
        return turnsTaken;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isSpectatorsAllowed() {
        return spectatorsAllowed;
    }

    public boolean isSpectatorsCanChat() {
        return spectatorsCanChat;
    }

    public boolean isSpectatorsCanSeeHand() {
        return spectatorsCanSeeHand;
    }

    private void getStats(GameReplayOuterClass.GameReplay replay) {
        this.replayID = replay.getReplayId();
        this.duration = replay.getDurationSeconds();

        this.players = new LinkedList<>();
        this.turnsTaken = 0;

        this.spectatorsAllowed = replay.getGameInfo().getSpectatorsAllowed();
        this.spectatorsCanChat = replay.getGameInfo().getSpectatorsCanChat();
        this.spectatorsCanSeeHand = replay.getGameInfo().getSpectatorsOmniscient();

        List<Integer> turnPlayerCache = new LinkedList<>();

        //For each event
        System.out.printf("[INFO] Reading replay file with %d event container objects...\n", replay.getEventListList().size());
        for (GameEventContainerOuterClass.GameEventContainer eventContainer : replay.getEventListList()) {
            if (eventContainer.getEventListList().size() > 0) {
                if (eventContainer.getContext().hasExtension(ContextDeckSelect.Context_DeckSelect.ext)) {
                    String hash = eventContainer.getContext().getExtension(ContextDeckSelect.Context_DeckSelect.ext).getDeckHash();
                    for (Player player : this.players) {
                        if (player.id == eventContainer.getEventListList().get(0).getPlayerId()) {
                            player.deckHash = hash;
                            break;
                        }
                    }
                }
            }

            List<GameEventOuterClass.GameEvent> events = eventContainer.getEventListList();
            for (GameEventOuterClass.GameEvent event : events) {
                int id = event.getPlayerId();
                if (event.hasExtension(EventSetActivePlayer.Event_SetActivePlayer.ext)) {
                    turnPlayerCache.add(id);
                    int i = 0;
                    for (Integer playerID : turnPlayerCache) {
                        if (id == (int) playerID) i++;
                        if (i >= 2) {
                            this.turnsTaken++;
                            turnPlayerCache = new LinkedList<>();
                            turnPlayerCache.add(id);
                            break;
                        }
                    }
                } else if (event.hasExtension(EventJoin.Event_Join.ext)) {
                    String name = event.getExtension(EventJoin.Event_Join.ext).getPlayerProperties().getUserInfo().getName();
                    id = event.getExtension(EventJoin.Event_Join.ext).getPlayerProperties().getPlayerId();
                    players.add(new Player(id, name));
                }
            }
        }

        this.playerCount = players.size();
    }

}