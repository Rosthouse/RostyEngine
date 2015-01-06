/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 *
 * @author Patrick
 */
public class PhysicsComponent<T extends Shape> extends Component {

    public final Body body;
    public final T shape;

    public PhysicsComponent(T shape, Body body, FixtureDef fixtureDef) {
        this.shape = shape;
        this.body = body;
    }

}
