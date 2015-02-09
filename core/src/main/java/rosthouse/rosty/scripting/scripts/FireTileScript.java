/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.scripting.scripts;

import rosthouse.rosty.components.FireComponent;
import rosthouse.rosty.components.WaterComponent;
import rosthouse.rosty.components.shader.ShaderComponent;
import rosthouse.rosty.collision.events.CollisionEvent;
import rosthouse.rosty.scripting.NativeScript;

/**
 *
 * @author Rosthouse
 */
public class FireTileScript extends NativeScript<CollisionEvent> {

    @Override
    public Object execute(CollisionEvent event) {

        if (event.other.getComponent(FireComponent.class) == null) {
            ShaderComponent t;
            if ((t = event.other.getComponent(ShaderComponent.class)) != null) {
                event.other.remove(ShaderComponent.class);
                t.dispose();
            }
            if (event.other.getComponent(WaterComponent.class) != null) {
                event.other.remove(WaterComponent.class);
            }
            event.other.add(new FireComponent());
            event.other.add(new ShaderComponent("shaders/fire"));
        }

        return null;
    }

}
