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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import rosthouse.rosty.components.PhysicsComponent;
import rosthouse.rosty.components.PositionComponent;
import rosthouse.rosty.components.VelocityComponent;

/**
 *
 * @author Patrick
 */
public class PhysicsSystem extends EntitySystem {

    private World world;
    private BodyDef bodyDef;
    float fixedStep;
    private ImmutableArray<Entity> entities;
    private final ComponentMapper<PhysicsComponent> cmPhysics = ComponentMapper.getFor(PhysicsComponent.class);
    private final ComponentMapper<PositionComponent> cmPosition = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<VelocityComponent> cmVelocity = ComponentMapper.getFor(VelocityComponent.class);

    public PhysicsSystem() {
        super();
        world = new World(new Vector2(0, 0), true);
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        fixedStep = 1 / 60f;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        entities = engine.getEntitiesFor(Family.getFor(PhysicsComponent.class, PositionComponent.class));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        world.step(fixedStep, 6, 2);
        for (int i = 0; i < entities.size(); i++) {
            Entity ent = entities.get(i);
            PositionComponent cmpPosition = cmPosition.get(ent);
            PhysicsComponent cmpPhysics = cmPhysics.get(ent);
            if (cmVelocity.has(ent)) {
                VelocityComponent cmpVelocity = cmVelocity.get(ent);
                cmpPhysics.body.applyForce(new Vector2(cmpVelocity.xAxis * cmpVelocity.speed, cmpVelocity.yAxis * cmpVelocity.speed), cmpPhysics.body.getPosition(), true);
            }
            cmpPosition.x = cmpPhysics.body.getPosition().x;
            cmpPosition.y = cmpPhysics.body.getPosition().y;
        }
    }

    public <T extends Shape> PhysicsComponent<T> createPhysicsComponent(BodyDef.BodyType type, T shape, Vector2 position, FixtureDef fixture) {
        fixture.shape = shape;
        bodyDef.position.set(position);
        Body body = world.createBody(bodyDef);
        body.setType(type);
        body.createFixture(fixture);
        return new PhysicsComponent<T>(shape, body, fixture);
    }

    public World getWorld() {
        return world;
    }
}
