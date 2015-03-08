/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.scripting.scripts;

import com.badlogic.gdx.ai.msg.MessageManager;
import java.util.List;
import static rosthouse.rosty.GameConstants.EventType.EndLevel;
import rosthouse.rosty.collision.events.CollisionEvent;
import rosthouse.rosty.scripting.Script;

/**
 *
 * @author Rosthouse
 */
public class EndLevelScript implements Script<CollisionEvent> {

    private final String nextLevelName;
    
    public EndLevelScript(List<String> parameters) {
        assert parameters.size() == 1;
        nextLevelName = parameters.get(0);
    }

    @Override
    public Object execute(CollisionEvent event) {
        MessageManager.getInstance().dispatchMessage(null, EndLevel.value, nextLevelName);
        return null;
    }

}
