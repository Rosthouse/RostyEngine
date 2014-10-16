/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author Patrick
 */
public class RenderComponent extends Component {

    public Texture texture;

    public RenderComponent(Texture texture) {
        this.texture = texture;
    }

}
