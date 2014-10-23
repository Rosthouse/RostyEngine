/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import rosthouse.rosty.components.PolygonComponent;
import rosthouse.rosty.components.PositionComponent;
import rosthouse.rosty.components.RenderComponent;
import rosthouse.rosty.components.TiledMapComponent;

/**
 *
 * @author Patrick
 */
public class RenderSystem extends EntitySystem {

    private final ComponentMapper<RenderComponent> cmRender = ComponentMapper.getFor(RenderComponent.class);
    private final ComponentMapper<PositionComponent> cmPosition = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<TiledMapComponent> cmTiledMap = ComponentMapper.getFor(TiledMapComponent.class);
    private final ComponentMapper<PolygonComponent> cmPolygon = ComponentMapper.getFor(PolygonComponent.class);
    private ImmutableArray<Entity> mapEntities;
    private ImmutableArray<Entity> movingEntities;
    private ImmutableArray<Entity> polygonEntities;
    private Batch batch;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    public RenderSystem(Batch batch, OrthographicCamera camera) {
        super();
        this.batch = batch;
        this.camera = camera;
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

    }

    @Override
    public void removedFromEngine(Engine engine) {
        mapEntities = null;
        movingEntities = null;
        batch = null;
        camera = null;
    }

    @Override
    public void addedToEngine(Engine engine) {
        mapEntities = engine.getEntitiesFor(Family.getFor(TiledMapComponent.class));
        polygonEntities = engine.getEntitiesFor(Family.getFor(PolygonComponent.class));
        movingEntities = engine.getEntitiesFor(Family.getFor(RenderComponent.class, PositionComponent.class));
        camera.update();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        OrthogonalTiledMapRenderer renderer = null;
        for (int i = 0; i < mapEntities.size(); i++) {
            TiledMapComponent cpMap = cmTiledMap.get(mapEntities.get(i));
            renderer = new OrthogonalTiledMapRenderer(cpMap.map, 1f / 32f);
            renderer.setView(camera);
            renderer.render();
        }
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        for (int i = 0; i < this.movingEntities.size(); i++) {
            PolygonComponent cpPolygon = cmPolygon.get(polygonEntities.get(i));
            shapeRenderer.polygon(cpPolygon.polygon.getTransformedVertices());
        }
        Gdx.gl20.glLineWidth(20);
        shapeRenderer.end();
        batch = renderer.getSpriteBatch();
        batch.begin();
        for (int i = 0; i < this.movingEntities.size(); i++) {
            PositionComponent cpPosition = cmPosition.get(movingEntities.get(i));
            RenderComponent cpRender = cmRender.get(movingEntities.get(i));
            renderer.getSpriteBatch().draw(cpRender.texture, cpPosition.x, cpPosition.y, cpRender.texture.getHeight() * renderer.getUnitScale(), cpRender.texture.getWidth() * renderer.getUnitScale());
        }
        batch.end();
    }
}
