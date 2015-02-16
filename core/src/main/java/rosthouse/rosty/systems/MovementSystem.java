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
        super(Family.getFor(PositionComponent.class, VelocityComponent.class));
        Gdx.app.log("MOVEMENTSYSTEM", "Loading Movement System");
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        VelocityComponent cpVelocity = cmMovement.get(entity);
        PositionComponent cpPosition = cmPosition.get(entity);
//        float xTranslation = cpVelocity.xAxis * deltaTime * cpVelocity.speed;
//        float yTranslation = cpVelocity.yAxis * deltaTime * cpVelocity.speed;
//        cpPosition.x += xTranslation;
//        cpPosition.y += yTranslation;

        if (cmCamera.has(entity)) {
            OrthographicCameraComponent cpCamera = cmCamera.get(entity);
            updateCamera((OrthographicCamera) cpCamera.camera, cpPosition.x, cpPosition.y, cpVelocity.zAxis * deltaTime);
        }
    }

    private void updateCamera(OrthographicCamera camera, float x, float y, float zoom) {
        camera.zoom += zoom;
        camera.translate(x, y, priority);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100 / camera.viewportWidth);
        camera.position.x = MathUtils.clamp(x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);
//        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
//        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);
//        Gdx.app.log("Camera Position", String.format("X: %f, Y: %f, Z: %f", camera.position.x, camera.position.y, camera.position.z));

    }

}
