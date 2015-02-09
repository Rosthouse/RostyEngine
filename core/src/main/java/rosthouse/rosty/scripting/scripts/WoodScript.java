/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.scripting.scripts;

import rosthouse.rosty.components.DeathComponent;
import rosthouse.rosty.components.FireComponent;
import rosthouse.rosty.collision.events.CollisionEvent;
import rosthouse.rosty.scripting.NativeScript;

/**
 *
 * @author Rosthouse
 */
public class WoodScript extends NativeScript<CollisionEvent> {

    @Override
    public Object execute(CollisionEvent event) {
        if (event.other.getComponent(FireComponent.class) != null) {
            event.self.add(new DeathComponent());
        }
        return null;
    }

}
