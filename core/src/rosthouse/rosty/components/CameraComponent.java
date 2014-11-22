/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Camera;

/**
 * Component containing a {@link Camera}
 *
 * @author Patrick
 */
public class CameraComponent extends Component {

    public final Camera camera;

    public CameraComponent(Camera camera) {
        this.camera = camera;
    }

}
