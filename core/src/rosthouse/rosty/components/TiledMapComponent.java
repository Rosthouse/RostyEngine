/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 *
 * @author Patrick
 */
public class TiledMapComponent extends Component {

    TiledMap map;

    public TiledMapComponent(TiledMap map) {
        this.map = map;
    }

}
