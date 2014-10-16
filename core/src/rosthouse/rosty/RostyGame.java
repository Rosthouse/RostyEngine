package rosthouse.rosty;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import rosthouse.rosty.entities.MovingPicture;
import rosthouse.rosty.systems.InputSystem;
import rosthouse.rosty.systems.MovementSystem;
import rosthouse.rosty.systems.RenderSystem;

public class RostyGame extends ApplicationAdapter {

    SpriteBatch batch;
    Engine engine;

    @Override
    public void create() {
        engine = new Engine();
        batch = new SpriteBatch();
        engine.addSystem(new InputSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderSystem(batch));
        engine.addEntity(new MovingPicture(new Texture("badlogic.jpg")));
    }

    @Override
    public void render() {
        engine.update(Gdx.graphics.getDeltaTime());
    }
}
