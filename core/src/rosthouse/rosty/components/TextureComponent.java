/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

/**
 * Component containing a {@link Texture}.
 *
 * @author Patrick
 */
public class TextureComponent extends Component {

    public final Texture texture;

    public TextureComponent(Texture texture) {
        this.texture = texture;
    }

}
