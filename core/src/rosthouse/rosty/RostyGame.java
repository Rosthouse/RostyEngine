package rosthouse.rosty;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import rosthouse.rosty.components.TiledMapComponent;
import rosthouse.rosty.entities.MovingPicture;
import rosthouse.rosty.systems.InputSystem;
import rosthouse.rosty.systems.MovementSystem;
import rosthouse.rosty.systems.RenderSystem;

public class RostyGame extends ApplicationAdapter {

    SpriteBatch batch;
    Engine engine;
    AssetManager assetManager;

    @Override
    public void create() {
        assetManager = new AssetManager();
        engine = new Engine();
        batch = new SpriteBatch();
        engine.addSystem(new InputSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderSystem(batch));
//        assetManager.load("C:\\Users\\Patrick\\Documents\\NetBeansProjects\\RostyEngine\\android\\assets\\data\\badlogic.jpg", Texture.class);
//        assetManager.finishLoading();
//        assetManager.update();
//        Texture tex = assetManager.get("badlogic.jpg", Texture.class);
        try {
            Texture tex = new Texture("badlogic.jpg");
            engine.addEntity(new MovingPicture(tex));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void loadMap() {
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load("maps/test.tmx", TiledMap.class);
        // once the asset manager is done loading
        TiledMap map = assetManager.get("maps/test.tmx");
        Entity mapEntity = new Entity();
        mapEntity.add(new TiledMapComponent(map));
        engine.addEntity(mapEntity);
    }

    @Override
    public void render() {
        engine.update(Gdx.graphics.getDeltaTime());
    }
}
