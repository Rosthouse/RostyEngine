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
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import rosthouse.rosty.GameConstants;
import rosthouse.rosty.MapObjects;
import rosthouse.rosty.components.OrthographicCameraComponent;
import rosthouse.rosty.components.PhysicsComponent;
import rosthouse.rosty.components.PositionComponent;
import rosthouse.rosty.components.ScriptComponent;
import rosthouse.rosty.components.SensorComponent;
import rosthouse.rosty.components.SpriteComponent;
import rosthouse.rosty.components.TiledMapComponent;
import rosthouse.rosty.components.collision.EllipseComponent;
import rosthouse.rosty.components.collision.PolygonComponent;
import rosthouse.rosty.components.collision.RectangleComponent;
import rosthouse.rosty.entities.MovingPicture;
import rosthouse.rosty.scripting.scripts.ClearMarbleScript;
import rosthouse.rosty.scripting.scripts.EndLevelScript;
import rosthouse.rosty.scripting.scripts.FireTileScript;
import rosthouse.rosty.scripting.scripts.WallCollisionScript;
import rosthouse.rosty.scripting.scripts.WaterTileScript;
import rosthouse.rosty.scripting.scripts.WoodScript;
import rosthouse.rosty.systems.PhysicsSystem;

/**
 * Loads a TMX map
 *
 * @author Rosthouse
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
//                Viewport viewPort = new FillViewport(50, 50, camera);
//                viewPort.apply();
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
            } else {
                Entity ent = new Entity();
                engine.addEntity(ent);
                Boolean isSensor = Boolean.valueOf(object.getProperties().get("isSensor", "false", String.class));
                loadObject(object, physicsSystem, ent, isSensor);
            }
        }
    }

    private void loadCollisionLayer(MapLayer layer, PhysicsSystem physicsSystem, Engine engine) {
        for (MapObject object : layer.getObjects()) {
            MapProperties properties = object.getProperties();
            Entity mapObjectEntity = new Entity();
            engine.addEntity(mapObjectEntity);
            loadObject(object, physicsSystem, mapObjectEntity, false);
            if (properties.containsKey(MapObjects.TYPE.toString())) {
                String type = properties.get(MapObjects.TYPE.toString(), String.class);
                if (type.equals("WoodTile")) {
                    ScriptComponent scriptComponent = new ScriptComponent();
                    scriptComponent.addScript(GameConstants.START_COLLISION, new WoodScript());
                    mapObjectEntity.add(scriptComponent);
                }
            }
            System.out.println(object);
        }
    }

    private void loadObject(MapObject object, PhysicsSystem physicsSystem, Entity mapObjectEntity, boolean isSensor) {
        if (object instanceof PolygonMapObject) {
            createPolygon((PolygonMapObject) object, physicsSystem, mapObjectEntity, isSensor);
        } else if (object instanceof RectangleMapObject) {
            createRectangle((RectangleMapObject) object, physicsSystem, mapObjectEntity, isSensor);
        } else if (object instanceof TextureMapObject) {
            createTexture((TextureMapObject) object, physicsSystem, mapObjectEntity, isSensor);
        } else if (object instanceof EllipseMapObject) {
            createEllipse((EllipseMapObject) object, physicsSystem, mapObjectEntity, isSensor);
        }
        if (object.getProperties().containsKey("onCollision")) {
            ScriptComponent script = createCollisionScript(object.getProperties().get("onCollision", "", String.class));
            mapObjectEntity.add(script);
        }
    }

    private void createPolygon(PolygonMapObject polygon, PhysicsSystem physicsSystem, Entity entity, boolean isSensor) {
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
        PhysicsComponent<ChainShape> cmpPhys;
        if (isSensor) {
            cmpPhys = physicsSystem.createSensorComponent(BodyDef.BodyType.StaticBody, polygonShape, new Vector2(ply.getX(), ply.getY()), fixtureDef);
        } else {
            cmpPhys = physicsSystem.createPhysicsComponent(BodyDef.BodyType.StaticBody, polygonShape, new Vector2(ply.getX(), ply.getY()), fixtureDef);
        }
        entity.add(plyCmp);
        entity.add(cmpPhys);
        cmpPhys.fixture.setUserData(entity.getId());

    }

    private void createTexture(TextureMapObject texture, PhysicsSystem physicsSystem, Entity mapObjectEntity, boolean isSensor) {

        SpriteComponent spriteComponent = new SpriteComponent(texture.getTextureRegion());
        PositionComponent positionComponent = new PositionComponent();
        PolygonShape polygonShape = new PolygonShape();

        float width = texture.getTextureRegion().getRegionWidth() * texture.getScaleX();
        float height = texture.getTextureRegion().getRegionHeight() * texture.getScaleY();
        float x = texture.getX() + width / 2;
        float y = texture.getY() + height / 2;

        spriteComponent.sprite.setX(x);
        spriteComponent.sprite.setY(y);
        spriteComponent.sprite.setRotation(texture.getRotation());
        spriteComponent.sprite.setScale(texture.getScaleX(), texture.getScaleY());
        positionComponent.x = x;
        positionComponent.y = y;
        positionComponent.rotation = (float) Math.toRadians(texture.getRotation());
        polygonShape.setAsBox(width * 0.5f,
                height * 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0;
        fixtureDef.friction = 1;
        fixtureDef.restitution = 0.2f;

        PhysicsComponent<PolygonShape> cmpSensor;
        if (isSensor) {
            cmpSensor = physicsSystem.createSensorComponent(BodyDef.BodyType.StaticBody, polygonShape, new Vector2(x, y), fixtureDef);
        } else {
            cmpSensor = physicsSystem.createPhysicsComponent(BodyDef.BodyType.StaticBody, polygonShape, new Vector2(x, y), fixtureDef);
        }
        cmpSensor.fixture.getBody().setTransform(x, y, texture.getRotation());
        mapObjectEntity.add(cmpSensor);
        cmpSensor.fixture.setUserData(mapObjectEntity.getId());
        mapObjectEntity.add(spriteComponent);
        mapObjectEntity.add(positionComponent);
    }

    private void createRectangle(RectangleMapObject rectangleObject, PhysicsSystem physicsSystem, Entity mapObjectEntity, boolean isSensor) {
        Rectangle rectangle = rectangleObject.getRectangle();
        RectangleComponent rectComponent = new RectangleComponent(rectangle);
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(rectangle.width * 0.5f, rectangle.height * 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0;
        fixtureDef.friction = 1;
        fixtureDef.restitution = 0.2f;
        PhysicsComponent<PolygonShape> cmpPhys;
        if (isSensor) {
            cmpPhys = physicsSystem.createSensorComponent(BodyDef.BodyType.StaticBody, polygonShape, rectangle.getCenter(Vector2.Zero), fixtureDef);
        } else {
            cmpPhys = physicsSystem.createPhysicsComponent(BodyDef.BodyType.StaticBody, polygonShape, rectangle.getCenter(Vector2.Zero), fixtureDef);
        }
        mapObjectEntity.add(rectComponent);
        mapObjectEntity.add(cmpPhys);
        cmpPhys.fixture.setUserData(mapObjectEntity.getId());
    }

    private void createEllipse(EllipseMapObject ellipseMapObject, PhysicsSystem physicsSystem, Entity entity, boolean isSensor) {
        Ellipse ellipse = ellipseMapObject.getEllipse();
        EllipseComponent cmpEllipse = new EllipseComponent(ellipse);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(ellipse.height / 2f);
//        circleShape.setPosition(new Vector2(ellipse.x, ellipse.y));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0;
        fixtureDef.friction = 1;
        fixtureDef.restitution = 0.2f;
        PhysicsComponent<CircleShape> cmpPhys;
        if (isSensor) {
            cmpPhys = physicsSystem.createSensorComponent(BodyDef.BodyType.StaticBody, circleShape, new Vector2(ellipse.x + ellipse.height / 2, ellipse.y + ellipse.height / 2), fixtureDef);
        } else {
            cmpPhys = physicsSystem.createPhysicsComponent(BodyDef.BodyType.StaticBody, circleShape, new Vector2(ellipse.x, ellipse.y), fixtureDef);
        }
        entity.add(cmpEllipse);
        entity.add(cmpPhys);
        cmpPhys.fixture.setUserData(entity.getId());
    }

    private void loadSensorLayer(MapLayer layer, PhysicsSystem physicsSystem, Engine engine) {
        for (MapObject object : layer.getObjects()) {
            MapProperties properties = object.getProperties();
            EllipseMapObject obj = (EllipseMapObject) object;
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(obj.getEllipse().height / 2);
            FixtureDef fd = new FixtureDef();
            fd.density = 5;
            fd.friction = 5;
            fd.restitution = 0.3f;
            Entity entity = new Entity();
            SensorComponent<CircleShape> sensor = physicsSystem.createSensorComponent(BodyDef.BodyType.StaticBody, circleShape, new Vector2(obj.getEllipse().x + obj.getEllipse().width / 2, obj.getEllipse().y + obj.getEllipse().height / 2), fd);
            engine.addEntity(entity.add(sensor));
            sensor.fixture.setUserData(entity.getId());
            ScriptComponent scriptComponent = new ScriptComponent();
            scriptComponent.addScript(GameConstants.END_COLLISION, new ClearMarbleScript());
            entity.add(scriptComponent);
//            }
        }
    }

    private ScriptComponent createCollisionScript(String collisionScriptName) {
        ScriptComponent cmpScript = new ScriptComponent();
        String scriptName = getScriptName(collisionScriptName);

        switch (scriptName) {
            case "FireScript":
                cmpScript.addScript(GameConstants.START_COLLISION, new FireTileScript());
                Gdx.app.debug("MAPLOADING", "Loading Fire script");
                break;
            case "WaterScript":
                cmpScript.addScript(GameConstants.START_COLLISION, new WaterTileScript());
                Gdx.app.debug("MAPLOADING", "Loading Water script");
                break;
            case "WoodScript":
                cmpScript.addScript(GameConstants.START_COLLISION, new WoodScript());
                Gdx.app.debug("MAPLOADING", "Loading Wood script");
                break;
            case "EndLevelScript":
                cmpScript.addScript(GameConstants.START_COLLISION, new EndLevelScript());
                Gdx.app.debug("MAPLOADING", "Loading Finish script");
                break;
            case "WallCollisionScript":
                cmpScript.addScript(GameConstants.POST_SOLVE, new WallCollisionScript());
                Gdx.app.debug("MAPLOADING", "Loading WallCollisionScript");
                break;
            default:
                break;
        }
        return cmpScript;
    }

    String getScriptName(String collisionScriptDefinition) {
        Pattern pattern = Pattern.compile("([a-z,A-Z]{1}[a-z,A-Z,0-9]*)[\\(]{0}");
        Matcher matcher = pattern.matcher(collisionScriptDefinition);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "";
        }
    }

    Collection<String> getScriptParameters(String collisionScriptDefinition) {
        Pattern pattern = Pattern.compile("\\(([ A-Za-z]+),([ A-Za-z]*)");
        Matcher matcher = pattern.matcher(collisionScriptDefinition);
        ArrayList<String> results = new ArrayList<String>();
        int groupCount = matcher.groupCount();
        for(int i = 1; i<= groupCount; i++){
            if(matcher.find(i)) {
                results.add(matcher.group(i));
            }
        }
        return results;
    }
}
