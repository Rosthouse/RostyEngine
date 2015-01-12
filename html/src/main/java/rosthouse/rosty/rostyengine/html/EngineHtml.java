package rosthouse.rosty.rostyengine.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import rosthouse.rosty.RostyGame;

public class EngineHtml extends GwtApplication {

    @Override
    public ApplicationListener getApplicationListener() {
        return new RostyGame();
    }

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(480, 320);
    }
}
