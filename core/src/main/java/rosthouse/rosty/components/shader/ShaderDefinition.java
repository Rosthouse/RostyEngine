/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components.shader;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 *Defines a  {@link Shader} shader and its variables (uniforms).
 * @author Rosthouse <rosthouse@gmail.com>
 */
public interface ShaderDefinition {
    
    public String getPath();
    public void applyUniformsToShaderProgram(ShaderProgram shader, float time);
    public void bindTextures(Sprite sprite);
    
}
