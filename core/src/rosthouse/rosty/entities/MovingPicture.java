/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import rosthouse.rosty.components.PositionComponent;
import rosthouse.rosty.components.RenderComponent;
import rosthouse.rosty.components.VelocityComponent;

/**
 *
 * @author Patrick
 */
public class MovingPicture extends Entity {

    public MovingPicture(Texture texture) {
        super();
        this.add(new PositionComponent());
        this.add(new VelocityComponent());
        this.add(new RenderComponent(texture));
    }
}
