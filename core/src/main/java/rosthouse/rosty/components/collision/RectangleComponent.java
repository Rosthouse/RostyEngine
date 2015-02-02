/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components.collision;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author Rosthouse
 */
public class RectangleComponent extends Component {

    public final Rectangle rectangle;

    public RectangleComponent(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

}
