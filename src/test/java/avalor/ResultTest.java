package avalor;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ResultTest {

    private Result result;

    @Before
    public void setUp() {
        this.result = new Result("(0,0)--(1,1)", 10.0);
    }

    @Test
    public void testGetPath() {
        assertEquals("(0,0)--(1,1)", result.getPath());
    }

    @Test
    public void testGetScore() {
        assertEquals(10.0, result.getScore(), 0.0);
    }

    @Test
    public void testToString() {
        assertEquals("Path: (0,0)--(1,1)\nScore: 10.0", result.toString());
    }
}
