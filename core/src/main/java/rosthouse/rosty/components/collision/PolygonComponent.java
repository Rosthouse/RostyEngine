/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components.collision;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Polygon;

/**
 * Component containing a {@link Polygon}.
 *
 * @author Patrick
 */
public class PolygonComponent extends Component {

    final public Polygon polygon;

    public PolygonComponent(Polygon polygon) {
        this.polygon = polygon;
    }

}
