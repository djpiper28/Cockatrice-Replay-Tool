import org.junit.Test;

import java.io.FileNotFoundException;

public class TestMain {
    @Test
    public void testMainForSingle() throws FileNotFoundException {
        Main.main(new String[] {"--replays", "replay.cor"});
    }

    @Test
    public void testMainForMany() throws FileNotFoundException {
        Main.main(new String[] {"--replays", "replay.cor", "replay.cor"});
    }

    @Test
    public void testMainForFolder() throws FileNotFoundException {
        Main.main(new String[] {"--replay-folder", "replays/"});
    }

    @Test
    public void stressTest() throws FileNotFoundException {
        long start = System.currentTimeMillis();
        Main.main(new String[] {"--replay-folder", "stress/"});
        System.out.printf("[TEST-RESULT] Time taken: %dms.\n", System.currentTimeMillis() - start);
    }
}
