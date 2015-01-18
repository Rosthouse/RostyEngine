/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.loader;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import rosthouse.rosty.MapObjects;
import rosthouse.rosty.components.OrthographicCameraComponent;
import rosthouse.rosty.components.PhysicsComponent;
import rosthouse.rosty.components.SensorComponent;
import rosthouse.rosty.components.SpriteComponent;
import rosthouse.rosty.components.TiledMapComponent;
import rosthouse.rosty.components.collision.PolygonComponent;
import rosthouse.rosty.components.collision.RectangleComponent;
import rosthouse.rosty.entities.MovingPicture;
import rosthouse.rosty.systems.PhysicsSystem;

/**
 * Loads a TMX map
 *
 * @author Pädda
 */
public class MapLoader {

    public void loadMap(String path, AssetManager assetManager, Engine engine, PhysicsSystem physicsSystem, final float unitScale) {

        TmxMapLoader.Parameters mapParameters = new TmxMapLoader.Parameters();
        mapParameters.convertObjectToTileSpace = true;

        assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        assetManager.load(path, TiledMap.class, mapParameters);
        assetManager.finishLoading();
        TiledMap map = assetManager.get(path);

        for (MapLayer layer : map.getLayers()) {
            String layerName = layer.getName();
            if (layerName.equals("Collisions")) {
                loadCollisionLayer(layer, physicsSystem, engine);
            } else if (layerName.equals("Level")) {
                loadLevelLayer(layer, unitScale, physicsSystem, engine);
            } else if (layerName.equals("Sensors")) {
                loadSensorLayer(layer, physicsSystem, engine);
            }
        }
        Entity mapEntity = new Entity();
        mapEntity.add(new TiledMapComponent(map, unitScale));
        engine.addEntity(mapEntity);

    }

    private void loadLevelLayer(MapLayer layer, final float unitScale, PhysicsSystem physicsSystem, Engine engine) {
        for (MapObject object : layer.getObjects()) {
            if (object.getName().equals("Start")) {
                float w = Gdx.graphics.getWidth();
                float h = Gdx.graphics.getHeight();
                OrthographicCamera camera = new OrthographicCamera();
                camera.setToOrtho(false, (w / h) * 10, 10);
                Texture tex = new Texture(Gdx.files.internal("Level/marble.png"));
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
                marble.fixture.setUserData(entity.getId());
            }
        }
    }

    private void loadCollisionLayer(MapLayer layer, PhysicsSystem physicsSystem, Engine engine) {
        for (MapObject object : layer.getObjects()) {
            MapProperties properties = object.getProperties();
            Entity mapObjectEntity = new Entity();
            engine.addEntity(mapObjectEntity);
            if (object instanceof PolygonMapObject) {
                createPolygon((PolygonMapObject) object, physicsSystem, mapObjectEntity);
            } else if (object instanceof RectangleMapObject) {
                createRectangle((RectangleMapObject) object, physicsSystem, mapObjectEntity);
            } else if (object instanceof TextureMapObject) {
                createTexture((TextureMapObject) object, physicsSystem, mapObjectEntity);
            }
            if (properties.containsKey(MapObjects.TYPE.toString())) {
                String type = properties.get(MapObjects.TYPE.toString(), String.class);

            }

            System.out.println(object);
        }
    }

    private void createPolygon(PolygonMapObject polygon, PhysicsSystem physicsSystem, Entity mapObjectEntity) {
        Polygon ply = polygon.getPolygon();
        PolygonComponent plyCmp = new PolygonComponent(ply);
        ChainShape polygonShape = new ChainShape();
        Vector2[] vertices = new Vector2[plyCmp.polygon.getVertices().length / 2];
        for (int i = 0; i < plyCmp.polygon.getTransformedVertices().length; i += 2) {
            vertices[i / 2] = new Vector2(plyCmp.polygon.getVertices()[i], plyCmp.polygon.getVertices()[i + 1]);
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
        PhysicsComponent<ChainShape> cmpPhys = physicsSystem.createPhysicsComponent(BodyDef.BodyType.StaticBody, polygonShape, new Vector2(ply.getX(), ply.getY()), fixtureDef);
        mapObjectEntity.add(cmpPhys);
        mapObjectEntity.add(plyCmp);
        cmpPhys.fixture.setUserData(mapObjectEntity.getId());
    }

    private void createTexture(TextureMapObject texture, PhysicsSystem physicsSystem, Entity mapObjectEntity) {

        SpriteComponent spriteComponent = new SpriteComponent(texture.getTextureRegion());
        PolygonShape polygonShape = new PolygonShape();
        
        float x = texture.getX();
        float y = texture.getY();
        float width = texture.getTextureRegion().getRegionWidth() *texture.getScaleX();
        float height = texture.getTextureRegion().getRegionHeight() * texture.getScaleY();
       
        polygonShape.setAsBox(width * 0.5f,
               height * 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0;
        fixtureDef.friction = 1;
        fixtureDef.restitution = 0.2f;
        PhysicsComponent<PolygonShape> cmpPhys = physicsSystem.createPhysicsComponent(BodyDef.BodyType.StaticBody, polygonShape, new Vector2(x, y), fixtureDef);
        mapObjectEntity.add(spriteComponent);
        mapObjectEntity.add(cmpPhys);
        cmpPhys.fixture.setUserData(mapObjectEntity.getId());
    }

    private void createRectangle(RectangleMapObject rectangleObject, PhysicsSystem physicsSystem, Entity mapObjectEntity) {
        Rectangle rectangle = rectangleObject.getRectangle();
        RectangleComponent rectComponent = new RectangleComponent(rectangle);
        PolygonShape polygonShape = new PolygonShape();       
        polygonShape.setAsBox(rectangle.width * 0.5f,
                rectangle.height * 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0;
        fixtureDef.friction = 1;
        fixtureDef.restitution = 0.2f;
        PhysicsComponent<PolygonShape> cmpPhys = physicsSystem.createPhysicsComponent(BodyDef.BodyType.StaticBody, polygonShape, rectangle.getCenter(Vector2.Zero), fixtureDef);
        mapObjectEntity.add(rectComponent);
        mapObjectEntity.add(cmpPhys);
        cmpPhys.fixture.setUserData(mapObjectEntity.getId());
    }

    private void loadSensorLayer(MapLayer layer, PhysicsSystem physicsSystem, Engine engine) {
        for (MapObject object : layer.getObjects()) {
            MapProperties properties = object.getProperties();
//            if ("Sensor".equals(properties.get("Typ", String.class))) {
            EllipseMapObject obj = (EllipseMapObject) object;
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(obj.getEllipse().height / 2);
            FixtureDef fd = new FixtureDef();
            fd.density = 5;
            fd.friction = 5;
            fd.restitution = 0.3f;
            Entity entity = new Entity();
            SensorComponent<CircleShape> sensor = physicsSystem.createSensorComponent(BodyDef.BodyType.StaticBody, circleShape, new Vector2(obj.getEllipse().x, obj.getEllipse().y), fd);
            engine.addEntity(entity.add(sensor));
            sensor.fixture.setUserData(entity.getId());
//            }
        }
    }

}
