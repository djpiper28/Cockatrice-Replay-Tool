import org.junit.Test;
import java.io.InputStream;

public class TestReplay {
    @Test
    public void testReplay() throws Exception {
        System.out.println("[TEST] Testing replay.java: Replay(File file)...");
        try {
            InputStream is = TestReplay.class.getClassLoader().getResourceAsStream("testReplay.cor");
            Replay replay = new Replay(is);

            System.out.printf("[TEST-INFO] Replay stats: %s\n", replay.toString());

            boolean isNullDeckHash = false;
            for (Replay.Player player: replay.getPlayers()) {
                if (player.deckHash == null) {
                    isNullDeckHash = true;
                    System.out.printf("[TEST-ERROR] %s has null hash\n", player.toString());
                }
            }

            boolean isNoID = false;
            for (Replay.Player player: replay.getPlayers()) {
                if (player.id == -1) {
                    isNoID = true;
                    System.out.printf("[TEST-ERROR] %s has no id\n", player.toString());
                }
            }

            if (isNoID | isNullDeckHash){
                System.out.println("[TEST] FAILURE - Invalid output.");
                throw new Exception("Test fail.");
            }

            assert (replay.getTurnsTaken() > 0);
            System.out.println("[TEST] SUCCESS");
        } catch (Exception e) {
            System.out.println("[TEST] FAILURE - Exception thrown.");
            e.printStackTrace();
            throw new Exception("Test fail.");
        }
    }
}
