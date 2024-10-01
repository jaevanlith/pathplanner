package avalor;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PathTupleTest {

    private PathTuple pathTuple;

    @Before
    public void setUp() {
        this.pathTuple = new PathTuple(0, 0, 10);
    }

    @Test
    public void testGetX() {
        assertEquals(0, pathTuple.getX());
    }

    @Test
    public void testGetY() {
        assertEquals(0, pathTuple.getY());
    }

    @Test
    public void testGetOriginalValue() {
        assertEquals(10, pathTuple.getOriginalValue());
    }
}
