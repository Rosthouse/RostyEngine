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
import rosthouse.rosty.components.OrthographicCameraComponent;
import rosthouse.rosty.components.PolygonComponent;

/**
 *
 * @author Pädda
 */
public class ShapeRenderSystem extends IteratingSystem {

    private ShapeRenderer shapeRenderer;
    private final ComponentMapper<OrthographicCameraComponent> cmCamera = ComponentMapper.getFor(OrthographicCameraComponent.class);
    private final ComponentMapper<PolygonComponent> cmPolygon = ComponentMapper.getFor(PolygonComponent.class);
    private ImmutableArray<Entity> polygonEntites;

    public ShapeRenderSystem() {
        super(Family.getFor(OrthographicCameraComponent.class));
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        polygonEntites = engine.getEntitiesFor(Family.getFor(PolygonComponent.class));
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine); //To change body of generated methods, choose Tools | Templates.
        polygonEntites = null;
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
        shapeRenderer.end();
    }

}
