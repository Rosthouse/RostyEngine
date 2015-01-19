/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components.shader;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;

/**
 *
 * @author Pädda
 */
public class ShaderComponent extends Component implements Disposable {

    public ShaderProgram shader;

    public ShaderComponent(String path) {
        shader = new ShaderProgram(Gdx.files.internal(path + ".vert"), Gdx.files.internal(path + ".frag"));

        Gdx.app.log("[SHADER]", shader.getLog());
    }

    @Override
    public void dispose() {
        shader.dispose();
        shader = null;
    }

}
