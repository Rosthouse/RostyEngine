/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.ObjectMap;
import rosthouse.rosty.scripting.Script;

/**
 *
 * @author Rosthouse
 */
public class ScriptComponent extends Component {

    public final ObjectMap<String, Script> scripts;

    public ScriptComponent() {
        this.scripts = new ObjectMap<String, Script>();
    }

    public void addScript(String name, Script script) {
        scripts.put(name, script);
    }

    public Script removeScript(String name) {
        return scripts.remove(name);
    }

    public Script getScript(String name) {
        return scripts.get(name);
    }

    public boolean hasScript(String name) {
        return scripts.containsKey(name);
    }
}
