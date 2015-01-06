/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import rosthouse.rosty.components.PositionComponent;
import rosthouse.rosty.components.TextureComponent;
import rosthouse.rosty.components.VelocityComponent;

/**
 * Entity, preconfigured with a {@link PositionComponent}, a
 * {@link VelocityComponent} and a {@link TextureComponent}.
 *
 * @author Patrick
 */
public class MovingPicture extends Entity {

    public MovingPicture(Texture texture) {
        this(texture, 0, 0);
    }

    public MovingPicture(Texture texture, float x, float y) {
        super();
        PositionComponent pos = new PositionComponent();
        pos.x = x;
        pos.y = y;
        this.add(pos);
        this.add(new VelocityComponent());
        this.add(new TextureComponent(texture));
    }
}
