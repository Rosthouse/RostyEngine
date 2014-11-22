package rosthouse.rosty;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Polygon;
import rosthouse.rosty.components.CameraComponent;
import rosthouse.rosty.components.PolygonComponent;
import rosthouse.rosty.components.TiledMapComponent;
import rosthouse.rosty.entities.MovingPicture;
import rosthouse.rosty.systems.InputSystem;
import rosthouse.rosty.systems.MovementSystem;
import rosthouse.rosty.systems.RenderSystem;

/**
 * Main class for the game. Starts all systems and loads the initial map.
 *
 * @author Patrick
 */
public class RostyGame extends ApplicationAdapter {

    Engine engine;
    AssetManager assetManager;

    private float unitScale = 1f / 32f;

    @Override
    public void create() {
        assetManager = new AssetManager();
        engine = new Engine();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, (w / h) * 10, 10);
        engine.addSystem(new InputSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderSystem(camera));
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
        assetManager.finishLoading();
        TiledMap map = assetManager.get("../android/assets/maps/test.tmx");
        for (MapLayer layer : map.getLayers()) {
            if (layer.getName().equals("Collisions")) {
                for (MapObject object : layer.getObjects()) {
                    if (object instanceof PolygonMapObject) {
                        PolygonMapObject obj = (PolygonMapObject) object;
                        Polygon ply = obj.getPolygon();
                        /**
                         * Sadly, Tiled doesn't save the position of objects
                         * relative to tiles, but rather in world space.
                         * Meaning, if you scale the world, the tiles will be
                         * scaled, but not the objects. To resolve this, you
                         * need to scale the object at runtime, as well as its
                         * position.
                         */
                        ply.setPosition(ply.getX() * unitScale, ply.getY() * unitScale);
                        ply.setScale(unitScale, unitScale);
                        PolygonComponent plyCmp = new PolygonComponent(ply);
                        engine.addEntity(new Entity().add(plyCmp));
                    }

                    System.out.println(object);
                }
            }
        }
        Entity mapEntity = new Entity();
        mapEntity.add(new TiledMapComponent(map, unitScale));
//        mapEntity.add(new TiledMapComponent(map, 1));
        engine.addEntity(mapEntity);
    }

    @Override
    public void render() {
        engine.update(Gdx.graphics.getDeltaTime());
    }
}
