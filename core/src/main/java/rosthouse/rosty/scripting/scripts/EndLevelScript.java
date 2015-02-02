/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.scripting.scripts;

import com.badlogic.gdx.ai.msg.MessageManager;
import static rosthouse.rosty.GameConstants.EventType.EndLevel;
import rosthouse.rosty.listener.CollisionEvent;
import rosthouse.rosty.scripting.Script;

/**
 *
 * @author Rosthouse
 */
public class EndLevelScript implements Script<CollisionEvent> {

    @Override
    public Object execute(CollisionEvent event) {
        MessageManager.getInstance().dispatchMessage(null, null, EndLevel.value);
        return null;
    }

}
