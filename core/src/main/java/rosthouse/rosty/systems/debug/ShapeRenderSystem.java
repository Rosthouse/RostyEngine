/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.systems.debug;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import rosthouse.rosty.components.OrthographicCameraComponent;
import rosthouse.rosty.components.SpriteComponent;
import rosthouse.rosty.components.collision.PolygonComponent;
import rosthouse.rosty.components.collision.RectangleComponent;

/**
 *
 * @author Pädda
 */
public class ShapeRenderSystem extends IteratingSystem {

    private ShapeRenderer shapeRenderer;
    private final ComponentMapper<OrthographicCameraComponent> cmCamera = ComponentMapper.getFor(OrthographicCameraComponent.class);
    private final ComponentMapper<PolygonComponent> cmPolygon = ComponentMapper.getFor(PolygonComponent.class);
    private final ComponentMapper<RectangleComponent> cmRectangle = ComponentMapper.getFor(RectangleComponent.class);
    private final ComponentMapper<SpriteComponent> cmSprite = ComponentMapper.getFor(SpriteComponent.class);
    private ImmutableArray<Entity> polygonEntites;
    private ImmutableArray<Entity> rectangleEntities;
    private ImmutableArray<Entity> spriteEntities;

    /**
     * Default contructor. Sets the system so that it won't be processed, so in
     * order to see shapes, you have to set {@link ShapeRenderSystem#setProcessing(boolean)
     * } to true.
     */
    public ShapeRenderSystem() {
        this(false);
    }

    /**
     * Creates a new ShapeRenderSystem.
     *
     * @param processing Wheter the system should be processed.
     */
    public ShapeRenderSystem(Boolean processing) {
        super(Family.getFor(OrthographicCameraComponent.class));
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        setProcessing(false);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        polygonEntites = engine.getEntitiesFor(Family.all(PolygonComponent.class).get());
        rectangleEntities = engine.getEntitiesFor(Family.all(RectangleComponent.class).get());
        spriteEntities = engine.getEntitiesFor(Family.all(SpriteComponent.class).get());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine); //To change body of generated methods, choose Tools | Templates.
        polygonEntites = null;
        rectangleEntities = null;
        spriteEntities = null;
        shapeRenderer.dispose();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {

        OrthographicCameraComponent cmpCamera = cmCamera.get(entity);

        shapeRenderer.setProjectionMatrix(cmpCamera.camera.combined);
        shapeRenderer.setColor(Color.BLUE);
        Gdx.gl20.glLineWidth(2);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < this.polygonEntites.size(); i++) {
            if (cmPolygon.has(this.polygonEntites.get(i))) {
                PolygonComponent cpPolygon = cmPolygon.get(polygonEntites.get(i));
                shapeRenderer.polygon(cpPolygon.polygon.getTransformedVertices());
            }
        }
        shapeRenderer.setColor(Color.RED);
        for (int i = 0; i < this.rectangleEntities.size(); i++) {
            if (cmRectangle.has(this.rectangleEntities.get(i))) {
                RectangleComponent cpRectangle = cmRectangle.get(rectangleEntities.get(i));
                shapeRenderer.rect(cpRectangle.rectangle.x, cpRectangle.rectangle.y, cpRectangle.rectangle.width, cpRectangle.rectangle.height);
            }
        }
        shapeRenderer.setColor(Color.GREEN);
        for (int i = 0; i < this.spriteEntities.size(); i++) {
            if (cmSprite.has(this.spriteEntities.get(i))) {
                SpriteComponent spSprite = cmSprite.get(spriteEntities.get(i));
                Rectangle boundingRectangle = spSprite.sprite.getBoundingRectangle();
                shapeRenderer.rect(boundingRectangle.x, boundingRectangle.y,boundingRectangle.width,boundingRectangle.height);
            }
        }
        shapeRenderer.end();
    }

}
