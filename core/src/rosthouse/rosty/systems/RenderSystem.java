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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;
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
    private ImmutableArray<Entity> mapEntities;
    private ImmutableArray<Entity> movingObjects;
    private SpriteBatch batch;
    List<Entity> renderQueue;

    public RenderSystem(SpriteBatch batch) {
        super();
        this.batch = batch;
        renderQueue = new ArrayList<Entity>();
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine); //To change body of generated methods, choose Tools | Templates.
        mapEntities = engine.getEntitiesFor(Family.getFor(TiledMapComponent.class));
        movingObjects = engine.getEntitiesFor(Family.getFor(RenderComponent.class, PositionComponent.class));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        for (Entity e : renderQueue) {
            PositionComponent cpPosition = cmPosition.get(e);
            batch.draw(cmRender.get(e).texture, cpPosition.x, cpPosition.y);
        }
        batch.end();
        renderQueue.clear();
    }
}
