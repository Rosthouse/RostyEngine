/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import rosthouse.rosty.components.PositionComponent;
import rosthouse.rosty.components.SpriteComponent;
import rosthouse.rosty.components.VelocityComponent;

/**
 * Entity, preconfigured with a {@link PositionComponent}, a
 * {@link VelocityComponent} and a {@link SpriteComponent}.
 *
 * @author Patrick
 */
public class MovingPicture extends Entity {

    private final SpriteComponent spriteComponent;
    private final PositionComponent positionComponent;

    public MovingPicture(Texture texture) {
        this(texture, 0, 0);
    }

    public MovingPicture(Texture texture, float x, float y) {
        super();
        this.spriteComponent = new SpriteComponent(texture);
        positionComponent = new PositionComponent();
        spriteComponent.sprite.setOrigin(texture.getWidth() / 2, texture.getHeight() / 2);

        positionComponent.x = x;
        positionComponent.y = y;
        this.add(positionComponent);
        this.add(new VelocityComponent());
        this.add(spriteComponent);
        spriteComponent.sprite.setOriginCenter();
    }

    public void setSize(Vector2 size) {
        this.spriteComponent.sprite.setSize(size.x, size.y);
        this.spriteComponent.sprite.setPosition(positionComponent.x, positionComponent.y);
    }
}
