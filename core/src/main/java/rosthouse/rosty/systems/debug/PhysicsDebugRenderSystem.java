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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import rosthouse.rosty.components.OrthographicCameraComponent;

/**
 *
 * @author Rosthouse
 */
public class PhysicsDebugRenderSystem extends IteratingSystem {

    private World debugWorld;
    private Box2DDebugRenderer debugRenderer = null;
    private final ComponentMapper<OrthographicCameraComponent> cmCamera = ComponentMapper.getFor(OrthographicCameraComponent.class);

    public PhysicsDebugRenderSystem(World world, Boolean processing) {
        super(Family.getFor(OrthographicCameraComponent.class));
        this.debugRenderer = new Box2DDebugRenderer();
        this.debugWorld = world;
        setProcessing(processing);
    }

    public PhysicsDebugRenderSystem(World world) {
        this(world, Boolean.FALSE);
        Gdx.app.debug("PHYSICSDEBUGSYSTEM", "Loading PhysicsDebug System");
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine); //To change body of generated methods, choose Tools | Templates.
        debugRenderer.dispose();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        OrthographicCameraComponent cmpCamera = cmCamera.get(entity);
        debugRenderer.render(debugWorld, cmpCamera.camera.combined);
    }

    /**
     * Sets the world which is to be rendered for debugging.
     *
     * @param world A initialized world.
     */
    public void setWorld(World world) {
        this.debugWorld = world;
    }

}
