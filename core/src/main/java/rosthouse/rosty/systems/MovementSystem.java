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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import rosthouse.rosty.components.OrthographicCameraComponent;
import rosthouse.rosty.components.PositionComponent;
import rosthouse.rosty.components.VelocityComponent;

/**
 * Moves {@link Entities} with a {@link PositionComponent} around.
 *
 * @author Patrick
 */
public class MovementSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> cmPosition = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> cmMovement = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<OrthographicCameraComponent> cmCamera = ComponentMapper.getFor(OrthographicCameraComponent.class);

    public MovementSystem() {
        super(Family.all(PositionComponent.class, VelocityComponent.class).get());
        Gdx.app.log("MOVEMENTSYSTEM", "Loading Movement System");
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        VelocityComponent cpVelocity = cmMovement.get(entity);
        PositionComponent cpPosition = cmPosition.get(entity);
        if (cmCamera.has(entity)) {
            updateCamera(cmCamera.get(entity), cpPosition.x, cpPosition.y, cpVelocity.zAxis * deltaTime);
        }
    }

    private void updateCamera(OrthographicCameraComponent cmCamera, float x, float y, float zoom) {
        OrthographicCamera camera = cmCamera.camera;
        camera.zoom += zoom;
        camera.translate(x, y, priority);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100 / camera.viewportWidth);
        camera.position.x = MathUtils.clamp(x, effectiveViewportWidth / 2f, cmCamera.getMapSize().x - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(y, effectiveViewportHeight / 2f, cmCamera.getMapSize().y - effectiveViewportHeight / 2f);
        Gdx.app.debug("CAMERA POSITION", String.format("X: %f; Y: %f", camera.position.x, camera.position.y));
    }
}
