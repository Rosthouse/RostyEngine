/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Pool;

/**
 *
 * @author Pädda
 */
public class ShaderComponent extends Component implements Pool.Poolable {

    public ShaderProgram shader;

    public ShaderComponent(String path) {
        shader = new ShaderProgram(Gdx.files.internal(path + ".vert"), Gdx.files.internal(path + ".frag"));

        Gdx.app.log("[SHADER]", shader.getLog());
    }

    @Override
    public void reset() {
        shader.dispose();
        shader = null;
    }

}
