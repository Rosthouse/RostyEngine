/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 *
 * @author Rosthouse
 */
public class SensorComponent<T extends Shape> extends PhysicsComponent {

//    public final T shape;
//    public final Fixture fixture;
    public SensorComponent(T shape, Fixture fixtureDef) {
        super(shape, fixtureDef);
    }
}
