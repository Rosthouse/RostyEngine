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
import rosthouse.rosty.components.SpriteComponent;
import rosthouse.rosty.components.shader.WaterShaderDefinition;
import rosthouse.rosty.scripting.NativeScript;

/**
 *
 * @author Rosthouse
 */
public class WaterTileScript extends NativeScript<CollisionEvent> {

    @Override
    public Object execute(CollisionEvent event) {

        ShaderComponent t;
        if (event.other.getComponent(WaterComponent.class) == null) {
            if ((t = event.other.getComponent(ShaderComponent.class)) != null) {
                event.other.remove(ShaderComponent.class);
                t.dispose();
            }
            if (event.other.getComponent(FireComponent.class) != null) {
                event.other.remove(FireComponent.class);
            }
            
            SpriteComponent sprite = event.other.getComponent(SpriteComponent.class);
            ShaderComponent cpShader = new ShaderComponent(new WaterShaderDefinition());
            event.other.add(cpShader);            
            event.other.add(new WaterComponent());
        }
        return null;
    }
}
