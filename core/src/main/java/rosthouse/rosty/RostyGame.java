package rosthouse.rosty;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import rosthouse.rosty.loader.MapLoader;
import rosthouse.rosty.systems.CleanUpSystem;
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
    CleanUpSystem cleanupSystem;
    private boolean reloadMap;

    private final float unitScale = 1f / 32f;

    private final class GameInputAdapter extends InputAdapter {

        @Override
        public boolean keyDown(int keycode) {

            if (keycode == Input.Keys.P || keycode == Input.Keys.MENU) {
                physicsDebugSystem.setProcessing(!physicsDebugSystem.checkProcessing());
                return true;
            } else if (keycode == Input.Keys.O || keycode == Input.Keys.MENU) {
                shapeRenderSystem.setProcessing(!shapeRenderSystem.checkProcessing());
            } else if (keycode == Input.Keys.ESCAPE) {
                dispose();
                RostyGame.this.dispose();
                return true;
            } else if (keycode == Input.Keys.F3 || keycode == Input.Keys.BACK) {
                reloadMap();
                return true;
            }

            return super.keyDown(keycode); //To change body of generated methods, choose Tools | Templates.
        }

    };

    private final class EngineTelegraph implements Telegraph {

        @Override
        public boolean handleMessage(Telegram tlgrm) {
            reloadMap = true;
            return false;
        }

    }

    @Override
    public void create() {
        setLogLevel(Application.LOG_DEBUG);
        assetManager = new AssetManager();
        engine = new Engine();
        renderSystem = new RenderSystem();
        movementSystem = new MovementSystem();
        inputSystem = new InputSystem();
        physicsSystem = new PhysicsSystem();
        physicsDebugSystem = new PhysicsDebugRenderSystem(physicsSystem.getWorld());
        shapeRenderSystem = new ShapeRenderSystem();
        cleanupSystem = new CleanUpSystem();

        inputSystem.addInputProcessor(new GameInputAdapter());
        engine.addSystem(inputSystem);
        engine.addSystem(physicsSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(physicsDebugSystem);
        engine.addSystem(shapeRenderSystem);
        engine.addSystem(cleanupSystem);

        MessageManager.getInstance().addListener(new EngineTelegraph(), GameConstants.EventType.EndLevel.value);
        loadMap("maps/Level1/Level1.tmx");
    }

    public void loadMap(String mapName) {
        MapLoader loader = new MapLoader();
        loader.loadMap(mapName, assetManager, engine, physicsSystem, unitScale);
    }

    public void setLogLevel(int logLevel) {
        Gdx.app.setLogLevel(logLevel);
    }

    private void reloadMap() {
        engine.removeAllEntities();
        physicsSystem.reloadWorld();
        physicsDebugSystem.setWorld(physicsSystem.getWorld());
        loadMap("maps/Level1/Level1.tmx");
    }

    @Override
    public void render() {
        engine.update(Gdx.graphics.getDeltaTime());
        if (reloadMap) {
            reloadMap();
            reloadMap = false;
        }
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
