/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 * Component containing a {@link Camera}
 *
 * @author Patrick
 */
public class OrthographicCameraComponent extends Component {

    public final OrthographicCamera camera;
    public final Vector2 mapSize;

    public OrthographicCameraComponent(OrthographicCamera camera, Vector2 mapSize) {
        this.camera = camera;
        this.mapSize = mapSize;
    }

    public Vector2 getMapSize() {
        return mapSize;
    }

}
