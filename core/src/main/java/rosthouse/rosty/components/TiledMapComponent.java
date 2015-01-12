/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Component containing a {@link TiledMap} and the corresponding
 * {@link BatchTiledMapRenderer}.
 *
 * @author Patrick
 */
public class TiledMapComponent extends Component {

    public final TiledMap map;
    public final OrthogonalTiledMapRenderer renderer;

    public TiledMapComponent(TiledMap map, float unitScale) {
        this.map = map;
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    }

}
