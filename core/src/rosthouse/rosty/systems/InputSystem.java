/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import rosthouse.rosty.components.VelocityComponent;

/**
 *
 * @author Patrick
 */
public class InputSystem extends IteratingSystem implements InputProcessor {

    private ComponentMapper<VelocityComponent> cmVelocity = ComponentMapper.getFor(VelocityComponent.class);
    private int upKey = Keys.W;
    private int downKey = Keys.S;
    private int leftKey = Keys.A;
    private int rightKey = Keys.D;
    private int riseKey = Keys.UP;
    private int lowerKey = Keys.DOWN;

    private int verticalModifier = 0;
    private int horizontalModifier = 0;
    private int zoomModifier = 0;

    public InputSystem() {
        super(Family.getFor(VelocityComponent.class));
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean handled = false;
        if (keycode == Keys.ESCAPE) {
            Gdx.app.exit();
        }
        if (keycode == upKey) {
            verticalModifier += 1;
            handled = true;
        }
        if (keycode == downKey) {
            verticalModifier -= 1;
            handled = true;
        }
        if (keycode == leftKey) {
            horizontalModifier -= 1;
            handled = true;
        }
        if (keycode == rightKey) {
            horizontalModifier += 1;
            handled = true;
        }
        if (keycode == lowerKey) {
            zoomModifier -= 1;
            handled = true;
        }
        if (keycode == riseKey) {
            zoomModifier += 1;
            handled = true;
        }

        return handled;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean handled = false;
        if (keycode == upKey) {
            verticalModifier -= 1;
            handled = true;
        }
        if (keycode == downKey) {
            verticalModifier += 1;
            handled = true;
        }
        if (keycode == leftKey) {
            horizontalModifier += 1;
            handled = true;
        }
        if (keycode == rightKey) {
            horizontalModifier -= 1;
            handled = true;
        }
        if (keycode == lowerKey) {
            zoomModifier += 1;
            handled = true;
        }
        if (keycode == riseKey) {
            zoomModifier -= 1;
            handled = true;
        }

        return handled;
    }

    @Override
    public boolean keyTyped(char character) {
        return false; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean scrolled(int amount) {
        return false; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        VelocityComponent cpVelocity = cmVelocity.get(entity);
        cpVelocity.xAxis = horizontalModifier;
        cpVelocity.yAxis = verticalModifier;
        cpVelocity.zAxis = zoomModifier;
    }

}
