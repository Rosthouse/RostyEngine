/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.components.shader;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 *
 * @author Rosthouse <rosthouse@gmail.com>
 */
public class WaterShaderDefinition implements ShaderDefinition{

    @Override
    public String getPath() {
        return "shaders/water_better";
    }

    @Override
    public void applyUniformsToShaderProgram(ShaderProgram shader, float time) {
        shader.setUniformf("Time", time);
        shader.setUniformf("Amplitude", 6);
        shader.setUniformf("WaveSpeed", 6);
    }
    
}
