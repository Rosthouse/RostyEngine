/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components.shader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 *
 * @author Rosthouse <rosthouse@gmail.com>
 */

public class FireShaderDefinition implements ShaderDefinition{

    @Override
    public String getPath() {
        return "shaders/fire";
    }

    @Override
    public void applyUniformsToShaderProgram(ShaderProgram shader, float time) {
      
    }


    
}
