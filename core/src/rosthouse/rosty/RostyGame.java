package rosthouse.rosty;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import rosthouse.rosty.components.OrthographicCameraComponent;
import rosthouse.rosty.components.PhysicsComponent;
import rosthouse.rosty.components.PolygonComponent;
import rosthouse.rosty.components.TiledMapComponent;
import rosthouse.rosty.entities.MovingPicture;
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

    private final float unitScale = 1f / 32f;

    private final class GameInputAdapter extends InputAdapter {

        @Override
        public boolean keyTyped(char character) {

            if (character == 'p') {
                if (physicsDebugSystem == null) {
                    physicsDebugSystem = new PhysicsDebugRenderSystem(physicsSystem.getWorld());
                    engine.addSystem(physicsDebugSystem);
                } else {
                    engine.removeSystem(physicsDebugSystem);
                    physicsDebugSystem = null;
                }
            } else if (character == 'o') {
                if (shapeRenderSystem == null) {
                    shapeRenderSystem = new ShapeRenderSystem();
                    engine.addSystem(shapeRenderSystem);
                } else {
                    engine.removeSystem(shapeRenderSystem);
                    shapeRenderSystem = null;
                }
            }
            return super.keyTyped(character); //To change body of generated methods, choose Tools | Templates.
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

        inputSystem.addInputProcessor(new GameInputAdapter());
        engine.addSystem(inputSystem);
        engine.addSystem(physicsSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(renderSystem);
        loadMap();
    }

    public void loadMap() {
        TmxMapLoader.Parameters paramters = new TmxMapLoader.Parameters();
        paramters.convertObjectToTileSpace = true;
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load("../android/assets/maps/test.tmx", TiledMap.class, paramters);
        assetManager.finishLoading();
        TiledMap map = assetManager.get("../android/assets/maps/test.tmx");
        for (MapLayer layer : map.getLayers()) {
            if (layer.getName().equals("Collisions")) {
                for (MapObject object : layer.getObjects()) {
                    if (object instanceof PolygonMapObject) {
                        PolygonMapObject obj = (PolygonMapObject) object;
                        Polygon ply = obj.getPolygon();
                        PolygonComponent plyCmp = new PolygonComponent(ply);

                        ChainShape polygonShape = new ChainShape();
                        Vector2[] vertices = new Vector2[plyCmp.polygon.getTransformedVertices().length / 2];
                        for (int i = 0; i < plyCmp.polygon.getTransformedVertices().length; i += 2) {
                            vertices[i / 2] = new Vector2(plyCmp.polygon.getTransformedVertices()[i], plyCmp.polygon.getTransformedVertices()[i + 1]);
                        }
                        try {
                            polygonShape.createLoop(vertices);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        FixtureDef fixtureDef = new FixtureDef();
                        fixtureDef.density = 0;
                        fixtureDef.friction = 1;
                        fixtureDef.restitution = 0.2f;
                        PhysicsComponent<ChainShape> cmpPhys = physicsSystem.createPhysicsComponent(BodyDef.BodyType.StaticBody, polygonShape, new Vector2(ply.getX() * unitScale, ply.getY() * unitScale), fixtureDef);
                        engine.addEntity(new Entity().add(plyCmp).add(cmpPhys));
                    }
                    System.out.println(object);
                }
            } else if (layer.getName().equals("Level")) {
                for (MapObject object : layer.getObjects()) {
                    if (object.getName().equals("Start")) {
                        float w = Gdx.graphics.getWidth();
                        float h = Gdx.graphics.getHeight();
                        OrthographicCamera camera = new OrthographicCamera();
                        camera.setToOrtho(false, (w / h) * 10, 10);
                        Texture tex = new Texture(Gdx.files.internal("../android/assets/level/marble.png"));
                        Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
                        MovingPicture entity = new MovingPicture(tex, ellipse.x, ellipse.y);

                        CircleShape circleShape = new CircleShape();
                        circleShape.setRadius((tex.getHeight() * unitScale) / 2);
                        FixtureDef fd = new FixtureDef();
                        fd.density = 5;
                        fd.friction = 5;
                        fd.restitution = 0.3f;
                        PhysicsComponent<CircleShape> marble = physicsSystem.createPhysicsComponent(BodyDef.BodyType.DynamicBody, circleShape, new Vector2(ellipse.x, ellipse.y), fd);
                        entity.add(marble);
                        entity.add(new OrthographicCameraComponent(camera));
                        engine.addEntity(entity);
                    }
                    if (object instanceof PolygonMapObject) {
                        PolygonMapObject obj = (PolygonMapObject) object;
                        Polygon ply = obj.getPolygon();
                        PolygonComponent plyCmp = new PolygonComponent(ply);
                        plyCmp.polygon.getTransformedVertices();

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
