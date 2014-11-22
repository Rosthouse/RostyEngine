/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Component representing a position for an {@link Entity}.
 *
 * @author Patrick
 */
public class PositionComponent extends Component {

    public float x = 0.0f;
    public float y = 0.0f;
}
