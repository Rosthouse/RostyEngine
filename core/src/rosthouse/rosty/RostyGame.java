package rosthouse.rosty;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import rosthouse.rosty.components.CameraComponent;
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
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, (w / h) * 10, 10);
        engine.addSystem(new InputSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderSystem(batch, camera));
        String localPath = Gdx.files.getLocalStoragePath();
        String internalPath = Gdx.files.getExternalStoragePath();
        try {
            Texture tex = new Texture(Gdx.files.local("../android/assets/badlogic.jpg"));
            MovingPicture entity = new MovingPicture(tex);
            entity.add(new CameraComponent(camera));
            engine.addEntity(entity);
            loadMap();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void loadMap() {
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new LocalFileHandleResolver()));
        assetManager.load("../android/assets/maps/test.tmx", TiledMap.class);
        // once the asset manager is done loading
        assetManager.finishLoading();
        TiledMap map = assetManager.get("../android/assets/maps/test.tmx");
        Entity mapEntity = new Entity();
        mapEntity.add(new TiledMapComponent(map));
        engine.addEntity(mapEntity);
    }

    @Override
    public void render() {
        engine.update(Gdx.graphics.getDeltaTime());
    }
}
