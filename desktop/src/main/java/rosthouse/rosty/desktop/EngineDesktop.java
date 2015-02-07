package rosthouse.rosty.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import rosthouse.rosty.RostyGame;

public class EngineDesktop {

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 480;
        config.width = 800;
//        config.fullscreen = true;
        new LwjglApplication(new RostyGame(), config);
    }
}
