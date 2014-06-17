package mathsquared.resultswizard2;

import java.awt.GraphicsEnvironment;

import org.junit.Test;

public class GraphicsDeviceSelectorTest {

    @Test
    public void testSelectGraphicsDevice () {
        GraphicsDeviceSelector.selectGraphicsDevice(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices());
    }

}
