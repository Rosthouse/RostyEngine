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
import rosthouse.rosty.components.CameraComponent;
import rosthouse.rosty.components.PositionComponent;
import rosthouse.rosty.components.VelocityComponent;

/**
 *
 * @author Patrick
 */
public class MovementSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> cmPosition = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> cmMovement = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<CameraComponent> cmCamera = ComponentMapper.getFor(CameraComponent.class);

    public MovementSystem() {
        super(Family.getFor(PositionComponent.class, VelocityComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        VelocityComponent cpVelocity = cmMovement.get(entity);
        PositionComponent cpPosition = cmPosition.get(entity);
        float xTranslation = cpVelocity.horizontal * deltaTime * cpVelocity.speed;
        float yTranslation = cpVelocity.vertical * deltaTime * cpVelocity.speed;
        cpPosition.x += xTranslation;
        cpPosition.y += yTranslation;

        if (cmCamera.has(entity)) {
            CameraComponent cpCamera = cmCamera.get(entity);
            cpCamera.camera.position.x = cpPosition.x;
            cpCamera.camera.position.y = cpPosition.y;
        }
    }

}
