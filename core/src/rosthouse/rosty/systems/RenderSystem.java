/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import rosthouse.rosty.components.PolygonComponent;
import rosthouse.rosty.components.PositionComponent;
import rosthouse.rosty.components.TextureComponent;
import rosthouse.rosty.components.TiledMapComponent;

/**
 * Renders everything to a screen.
 *
 * @author Patrick
 */
public class RenderSystem extends EntitySystem implements EntityListener {

    private final ComponentMapper<TextureComponent> cmRender = ComponentMapper.getFor(TextureComponent.class);
    private final ComponentMapper<PositionComponent> cmPosition = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<TiledMapComponent> cmTiledMap = ComponentMapper.getFor(TiledMapComponent.class);
    private final ComponentMapper<PolygonComponent> cmPolygon = ComponentMapper.getFor(PolygonComponent.class);
    private ImmutableArray<Entity> spriteEntites;
    private ImmutableArray<Entity> polygonEntites;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private TiledMapComponent cpMap;

    public RenderSystem(OrthographicCamera camera) {
        super();
        this.spriteBatch = new SpriteBatch();
        this.camera = camera;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        spriteEntites = null;
        spriteBatch = null;
        camera = null;
        spriteEntites = null;
    }

    @Override
    public void addedToEngine(Engine engine) {
        engine.addEntityListener(this);
        spriteEntites = engine.getEntitiesFor(Family.getFor(ComponentType.getBitsFor(PositionComponent.class), ComponentType.getBitsFor(TextureComponent.class, PolygonComponent.class), ComponentType.getBitsFor()));
        polygonEntites = engine.getEntitiesFor(Family.getFor(PolygonComponent.class));
        camera.update();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        cpMap.renderer.setView(camera);

        cpMap.renderer.render();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (int i = 0; i < this.spriteEntites.size(); i++) {
            PositionComponent cpPosition = cmPosition.get(spriteEntites.get(i));
            TextureComponent cpRender = cmRender.get(spriteEntites.get(i));
            spriteBatch.draw(cpRender.texture, cpPosition.x, cpPosition.y, cpRender.texture.getHeight() * cpMap.renderer.getUnitScale(), cpRender.texture.getWidth() * cpMap.renderer.getUnitScale());
//            spriteBatch.draw(cpRender.texture, cpPosition.x, cpPosition.y, cpRender.texture.getHeight(), cpRender.texture.getWidth());
        }
        spriteBatch.end();
        renderShapes(camera.combined);
    }

    void renderShapes(Matrix4 projectionMatrix) {
        shapeRenderer.setProjectionMatrix(projectionMatrix);
        shapeRenderer.setColor(Color.BLUE);
        Gdx.gl20.glLineWidth(2);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < this.polygonEntites.size(); i++) {
            if (cmPolygon.has(this.polygonEntites.get(i))) {
                PolygonComponent cpPolygon = cmPolygon.get(polygonEntites.get(i));
                shapeRenderer.polygon(cpPolygon.polygon.getTransformedVertices());
            }
        }
        shapeRenderer.end();
    }

    @Override
    public void entityAdded(Entity entity) {
        if (cmTiledMap.has(entity)) {
            cpMap = cmTiledMap.get(entity);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (cmTiledMap.has(entity)) {
            cpMap = null;
        }
    }

}
