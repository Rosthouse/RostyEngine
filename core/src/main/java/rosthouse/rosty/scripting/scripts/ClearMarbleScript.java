/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.scripting.scripts;

import rosthouse.rosty.components.shader.ShaderComponent;
import rosthouse.rosty.listener.CollisionEvent;
import rosthouse.rosty.scripting.NativeScript;

/**
 *
 * @author Pädda
 */
public class ClearMarbleScript extends NativeScript<CollisionEvent> {

    @Override
    public Object execute(CollisionEvent event) {
        ShaderComponent shader = event.other.getComponent(ShaderComponent.class);
        if (shader != null) {
            event.other.remove(ShaderComponent.class);
            shader.shader.dispose();
        }
        return null;
    }

}
