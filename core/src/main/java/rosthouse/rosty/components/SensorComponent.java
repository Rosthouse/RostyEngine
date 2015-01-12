/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 *
 * @author Pädda
 */
public class SensorComponent<T> extends Component {

    public final T shape;
    public final Fixture fixture;

    public SensorComponent(T shape, Fixture fixtureDef) {
        this.fixture = fixtureDef;
        this.shape = shape;
    }
}
