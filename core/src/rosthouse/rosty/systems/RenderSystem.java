/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;
import rosthouse.rosty.components.PositionComponent;
import rosthouse.rosty.components.RenderComponent;

/**
 *
 * @author Patrick
 */
public class RenderSystem extends IteratingSystem {

    private final ComponentMapper<RenderComponent> cmRender = ComponentMapper.getFor(RenderComponent.class);
    private final ComponentMapper<PositionComponent> cmPosition = ComponentMapper.getFor(PositionComponent.class);
    private SpriteBatch batch;
    List<Entity> renderQueue;

    public RenderSystem(SpriteBatch batch) {
        super(Family.getFor(RenderComponent.class, PositionComponent.class));
        this.batch = batch;
        renderQueue = new ArrayList<Entity>();
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

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

}
