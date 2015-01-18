package rosthouse.rosty;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import rosthouse.rosty.listener.CollisionListener;
import rosthouse.rosty.loader.MapLoader;
import rosthouse.rosty.systems.InputSystem;
import rosthouse.rosty.systems.MovementSystem;
import rosthouse.rosty.systems.PhysicsSystem;
import rosthouse.rosty.systems.RenderSystem;
import rosthouse.rosty.systems.debug.PhysicsDebugRenderSystem;
import rosthouse.rosty.systems.debug.ShapeRenderSystem;

/**
 * Main class for the game. Starts all systems and loads the initial map.
 *
 * @author Patrick
 */
public class RostyGame extends ApplicationAdapter {

    Engine engine;
    AssetManager assetManager;
    RenderSystem renderSystem;
    MovementSystem movementSystem;
    InputSystem inputSystem;
    PhysicsSystem physicsSystem;
    PhysicsDebugRenderSystem physicsDebugSystem;
    ShapeRenderSystem shapeRenderSystem;
    CollisionListener collList;

    private final float unitScale = 1f / 32f;

    private final class GameInputAdapter extends InputAdapter {

        @Override
        public boolean keyDown(int keycode) {

            if (keycode == Input.Keys.P) {
                physicsDebugSystem.setProcessing(!physicsDebugSystem.checkProcessing());
                return true;
            } else if (keycode == Input.Keys.O) {
                shapeRenderSystem.setProcessing(!shapeRenderSystem.checkProcessing());
            } else if (keycode == Input.Keys.ESCAPE) {
                dispose();
                RostyGame.this.dispose();
                return true;
            } else if (keycode == Input.Keys.F3) {
                engine.removeAllEntities();
                loadMap();
                return true;
            }

            return super.keyDown(keycode); //To change body of generated methods, choose Tools | Templates.
        }

    };

    @Override
    public void create() {
        assetManager = new AssetManager();
        engine = new Engine();
        renderSystem = new RenderSystem(null);
        movementSystem = new MovementSystem();
        inputSystem = new InputSystem();
        physicsSystem = new PhysicsSystem();
        physicsDebugSystem = new PhysicsDebugRenderSystem(physicsSystem.getWorld());
        shapeRenderSystem = new ShapeRenderSystem();

        inputSystem.addInputProcessor(new GameInputAdapter());
        engine.addSystem(inputSystem);
        engine.addSystem(physicsSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(physicsDebugSystem);
        engine.addSystem(shapeRenderSystem);
        loadMap();

        collList = new CollisionListener(engine);
        physicsSystem.getWorld().setContactListener(collList);
    }

    public void loadMap() {
        MapLoader loader = new MapLoader();
        loader.loadMap("maps/test.tmx", assetManager, engine, physicsSystem, unitScale);
    }

    @Override
    public void render() {
        engine.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        engine.removeAllEntities();
        engine.removeSystem(inputSystem);
        engine.removeSystem(physicsSystem);
        engine.removeSystem(movementSystem);
        engine.removeSystem(renderSystem);
        assetManager.dispose();
        Gdx.app.exit();
    }

}
