/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.ObjectMap;

/**
 *
 * @author P�dda
 */
public class PropertiesComponent extends Component{
    public final ObjectMap<String, Object> properties;

    public PropertiesComponent() {
        this(new ObjectMap<String, Object>());
    }
    
    public PropertiesComponent(ObjectMap<String, Object> properties) {
        this.properties = properties;
    }
}
