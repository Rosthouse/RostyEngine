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
import com.badlogic.gdx.utils.TimeUtils;
import rosthouse.rosty.components.OrthographicCameraComponent;
import rosthouse.rosty.components.PositionComponent;
import rosthouse.rosty.components.SpriteComponent;
import rosthouse.rosty.components.TiledMapComponent;
import rosthouse.rosty.components.collision.PolygonComponent;
import rosthouse.rosty.components.shader.ShaderComponent;

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
    private SpriteBatch spriteBatch;
    private TiledMapComponent cpMap;
    private float timeSinceStart = 0;

    public RenderSystem() {
        super();
        Gdx.app.log("RENDERSYSTEM", "Loading Rendering System");
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
        spriteEntites = engine.getEntitiesFor(Family.all(PositionComponent.class).one(SpriteComponent.class, PolygonComponent.class).get());
        cameraEntities = engine.getEntitiesFor(Family.all(OrthographicCameraComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        timeSinceStart += deltaTime;
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
            renderSprite(entity);
        }
        spriteBatch.end();
    }

    private void renderSprite(Entity entity) {
        PositionComponent cpPosition = cmPosition.get(entity);
        SpriteComponent cpRender = cmRender.get(entity);
        cpRender.sprite.setCenter(cpPosition.x, cpPosition.y);
        cpRender.sprite.setScale(cpMap.renderer.getUnitScale());
        cpRender.sprite.setRotation((float) Math.toDegrees(cpPosition.rotation));
        if (cmShader.has(entity)) {
            renderShader(entity, cpRender);
        } else {
            cpRender.sprite.draw(spriteBatch);
        }
    }

    private void renderShader(Entity entity, SpriteComponent cpRender) {
        ShaderComponent cpShader = cmShader.get(entity);
        if (cpShader.shader.isCompiled()) {
            spriteBatch.setShader(cpShader.shader); 
            cpShader.definition.applyUniformsToShaderProgram(cpShader.shader, timeSinceStart);
            cpRender.sprite.getTexture().bind(0);
            cpRender.sprite.draw(spriteBatch);
            spriteBatch.setShader(null);
        }
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
