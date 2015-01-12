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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import rosthouse.rosty.components.OrthographicCameraComponent;
import rosthouse.rosty.components.PositionComponent;
import rosthouse.rosty.components.ShaderComponent;
import rosthouse.rosty.components.SpriteComponent;
import rosthouse.rosty.components.TiledMapComponent;
import rosthouse.rosty.components.collision.PolygonComponent;

/**
 * Renders everything to a screen.
 *
 * @author Patrick
 */
public class RenderSystem extends EntitySystem implements EntityListener {

    private final ComponentMapper<SpriteComponent> cmRender = ComponentMapper.getFor(SpriteComponent.class);
    private final ComponentMapper<PositionComponent> cmPosition = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<TiledMapComponent> cmTiledMap = ComponentMapper.getFor(TiledMapComponent.class);
    private final ComponentMapper<OrthographicCameraComponent> cmCamera = ComponentMapper.getFor(OrthographicCameraComponent.class);
    private final ComponentMapper<ShaderComponent> cmShader = ComponentMapper.getFor(ShaderComponent.class);
    private ImmutableArray<Entity> spriteEntites;
    private ImmutableArray<Entity> cameraEntities;
    private ImmutableArray<Entity> shaderEntities;
    private SpriteBatch spriteBatch;
    private TiledMapComponent cpMap;

    public RenderSystem(OrthographicCamera camera) {
        super();
        this.spriteBatch = new SpriteBatch();
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(this);
        spriteEntites = null;
        spriteBatch = null;
        spriteEntites = null;

    }

    @Override
    public void addedToEngine(Engine engine) {
        engine.addEntityListener(this);
        spriteEntites = engine.getEntitiesFor(Family.getFor(ComponentType.getBitsFor(PositionComponent.class), ComponentType.getBitsFor(SpriteComponent.class, PolygonComponent.class), ComponentType.getBitsFor()));
        cameraEntities = engine.getEntitiesFor(Family.getFor(OrthographicCameraComponent.class));
        shaderEntities = engine.getEntitiesFor(Family.getFor(ShaderComponent.class));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for (int i = 0; i < cameraEntities.size(); i++) {
            OrthographicCamera camera = cmCamera.get(cameraEntities.get(i)).camera;
            camera.update();
            renderCamera(camera);
        }

    }

    /**
     * Renders a camera.
     *
     * @param camera
     */
    private void renderCamera(OrthographicCamera camera) {
        spriteBatch.setShader(null);
        cpMap.renderer.setView(camera);
        cpMap.renderer.render();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (Entity entity : spriteEntites) {
            PositionComponent cpPosition = cmPosition.get(entity);
            SpriteComponent cpRender = cmRender.get(entity);
            cpRender.sprite.setCenter(cpPosition.x, cpPosition.y);
            cpRender.sprite.setScale(cpMap.renderer.getUnitScale());
            cpRender.sprite.setRotation((float) Math.toDegrees(cpPosition.rotation));
            if (cmShader.has(entity)) {
                ShaderComponent cpShader = cmShader.get(entity);
                if (cpShader.shader.isCompiled()) {
                    spriteBatch.setShader(cpShader.shader);
                    cpRender.sprite.draw(spriteBatch);
                }
            } else {

                cpRender.sprite.draw(spriteBatch);
            }
        }
        spriteBatch.end();
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
            TiledMapComponent cmpTiledMap = cmTiledMap.get(entity);
            cmpTiledMap.map.dispose();
        }
        if (cmShader.has(entity)) {
            ShaderComponent cmpShader = cmShader.get(entity);
            cmpShader.shader.dispose();
        }
    }

}
