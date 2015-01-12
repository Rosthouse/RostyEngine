/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Component containing a {@link Sprite}.
 *
 * @author Patrick
 */
public class SpriteComponent extends Component {

    public final Sprite sprite;

    public SpriteComponent(Texture texture) {
        this.sprite = new Sprite(texture);
    }

}
