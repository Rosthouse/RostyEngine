/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components.collision;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Ellipse;

/**
 *
 * @author Rosthouse
 */
public class EllipseComponent extends Component {

    public final Ellipse ellipse;

    public EllipseComponent(Ellipse ellipse) {
        this.ellipse = ellipse;
    }

}
