/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.scripting.scripts;

import rosthouse.rosty.components.FireComponent;
import rosthouse.rosty.components.shader.ShaderComponent;
import rosthouse.rosty.listener.CollisionEvent;
import rosthouse.rosty.scripting.NativeScript;

/**
 *
 * @author Pädda
 */
public class WaterTileScript extends NativeScript<CollisionEvent> {

    @Override
    public Object execute(CollisionEvent event) {

        ShaderComponent t;
        if ((t = event.other.getComponent(ShaderComponent.class)) != null) {
            event.other.remove(ShaderComponent.class);
            t.dispose();
        }
        if (event.other.getComponent(FireComponent.class) != null) {
            event.other.remove(FireComponent.class);
        }
        event.other.add(new ShaderComponent("shaders/water"));
        return null;
    }
}
