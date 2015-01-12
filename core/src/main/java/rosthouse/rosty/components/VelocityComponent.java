/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.ashley.core.Component;

/**
 * Represents the velocity of a Component.
 *
 * @author Patrick
 */
public class VelocityComponent extends Component {

    public int xAxis;
    public int yAxis;
    public int zAxis;
    public int speed;

    public VelocityComponent() {
        this(0, 0, 0, 80);
    }

    public VelocityComponent(int speed) {
        this(0, 0, 0, speed);
    }

    public VelocityComponent(int xAxis, int yAxis, int zAxis, int speed) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.zAxis = zAxis;
        this.speed = speed;
    }

}
