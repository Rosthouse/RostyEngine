/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.collision;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import rosthouse.rosty.GameConstants;
import rosthouse.rosty.collision.events.CollisionEvent;
import rosthouse.rosty.collision.events.PostSolveCollisionEvent;
import rosthouse.rosty.collision.events.PreSolveCollisionEvent;
import rosthouse.rosty.components.ScriptComponent;
import rosthouse.rosty.scripting.Script;

/**
 *
 * @author Rosthouse
 */
public class CollisionListener implements ContactListener {

    private final Engine engine;

    public CollisionListener(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void beginContact(Contact cntct) {
        Long idA = (Long) cntct.getFixtureA().getUserData();
        Long idB = (Long) cntct.getFixtureB().getUserData();
        Entity entityA = engine.getEntity(idA);
        Entity entityB = engine.getEntity(idB);
        ScriptComponent scriptComponent = null;
        if ((scriptComponent = entityA.getComponent(ScriptComponent.class)) != null) {
            handleStartCollision(scriptComponent, cntct, entityA, entityB);
        }
        if ((scriptComponent = entityB.getComponent(ScriptComponent.class)) != null) {
            handleStartCollision(scriptComponent, cntct, entityB, entityA);
        }
    }

    public void handleStartCollision(ScriptComponent scriptComponent, Contact contact, Entity self, Entity other) {
        if (scriptComponent.hasScript(GameConstants.START_COLLISION)) {
            Script script = scriptComponent.getScript(GameConstants.START_COLLISION);
            CollisionEvent collisionEvent = new CollisionEvent(contact, self, other);
            script.execute(collisionEvent);
        }
    }

    @Override
    public void endContact(Contact cntct) {

        Long idA = (Long) cntct.getFixtureA().getUserData();
        Long idB = (Long) cntct.getFixtureB().getUserData();
        Entity entityA = engine.getEntity(idA);
        Entity entityB = engine.getEntity(idB);
        ScriptComponent scriptComponent = null;
        if (entityA != null && (scriptComponent = entityA.getComponent(ScriptComponent.class)) != null) {
            handleEndCollision(scriptComponent, cntct, entityA, entityB);
        }
        if (entityB != null && (scriptComponent = entityB.getComponent(ScriptComponent.class)) != null) {
            handleEndCollision(scriptComponent, cntct, entityB, entityA);
        }

    }

    public void handleEndCollision(ScriptComponent scriptComponent, Contact contact, Entity self, Entity other) {
        if (scriptComponent.hasScript(GameConstants.END_COLLISION)) {
            Script script = scriptComponent.getScript(GameConstants.END_COLLISION);
            script.execute(new CollisionEvent(contact, self, other));
        }
    }

    @Override
    public void preSolve(Contact cntct, Manifold mnfld) {

        Long idA = (Long) cntct.getFixtureA().getUserData();
        Long idB = (Long) cntct.getFixtureB().getUserData();
        Entity entityA = engine.getEntity(idA);
        Entity entityB = engine.getEntity(idB);
        ScriptComponent scriptComponent = null;
        if (entityA != null && (scriptComponent = entityA.getComponent(ScriptComponent.class)) != null) {
            handlePreSolve(scriptComponent, cntct, mnfld, entityA, entityB);
        }
        if (entityB != null && (scriptComponent = entityB.getComponent(ScriptComponent.class)) != null) {
            handleEndCollision(scriptComponent, cntct, entityB, entityA);
        }

        return;
    }

    @Override
    public void postSolve(Contact cntct, ContactImpulse ci) {

        Long idA = (Long) cntct.getFixtureA().getUserData();
        Long idB = (Long) cntct.getFixtureB().getUserData();
        Entity entityA = engine.getEntity(idA);
        Entity entityB = engine.getEntity(idB);
        ScriptComponent scriptComponent = null;
        if (entityA != null && (scriptComponent = entityA.getComponent(ScriptComponent.class)) != null) {
            handlePostSolve(scriptComponent, cntct, ci, entityA, entityB);
        }
        if (entityB != null && (scriptComponent = entityB.getComponent(ScriptComponent.class)) != null) {
            handlePostSolve(scriptComponent, cntct, ci, entityA, entityB);
        }
        return;
    }

    private void handlePostSolve(ScriptComponent scriptComponent, Contact contact, ContactImpulse impulse, Entity self, Entity other) {
        if (scriptComponent.hasScript(GameConstants.POST_SOLVE)) {
            Script script = scriptComponent.getScript(GameConstants.POST_SOLVE);
            script.execute(new PostSolveCollisionEvent(contact, impulse, self, other));
        }
    }

    private void handlePreSolve(ScriptComponent scriptComponent, Contact contact, Manifold manifold, Entity self, Entity other) {
        if (scriptComponent.hasScript(GameConstants.PRE_SOLVE)) {
            Script script = scriptComponent.getScript(GameConstants.PRE_SOLVE);
            script.execute(new PreSolveCollisionEvent(contact, manifold, self, other));
        }
    }

}
