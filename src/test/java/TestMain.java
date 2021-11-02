import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

public class TestMain {

    @Test
    public void stressTest() throws FileNotFoundException {
        long start = System.currentTimeMillis();
        for (File file: new File("stress/").listFiles()) {
            Main.main(new String[] {file.getAbsolutePath()});
        }
        System.out.printf("[TEST-RESULT] Time taken: %dms.\n", System.currentTimeMillis() - start);
    }

}
