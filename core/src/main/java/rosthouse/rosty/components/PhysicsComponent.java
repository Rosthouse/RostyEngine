/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 *
 * @author Patrick
 */
public class PhysicsComponent<T extends Shape> extends Component {

    public final Fixture fixture;
    public final T shape;

    public PhysicsComponent(T shape, Fixture fixture) {
        this.shape = shape;
        this.fixture = fixture;
    }

}
