/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.listener;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import rosthouse.rosty.GameConstants;
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
            handleStartCollision(scriptComponent, entityA, entityB);
        }
        if ((scriptComponent = entityB.getComponent(ScriptComponent.class)) != null) {
            handleStartCollision(scriptComponent, entityB, entityA);
        }
    }

    public void handleStartCollision(ScriptComponent scriptComponent, Entity self, Entity other) {
        if (scriptComponent.hasScript(GameConstants.START_COLLISION)) {
            Script script = scriptComponent.getScript(GameConstants.START_COLLISION);
            CollisionEvent collisionEvent = new CollisionEvent(self, other);
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
            handleEndCollision(scriptComponent, entityA, entityB);
        }
        if (entityB != null && (scriptComponent = entityB.getComponent(ScriptComponent.class)) != null) {
            handleEndCollision(scriptComponent, entityB, entityA);
        }

    }

    public void handleEndCollision(ScriptComponent scriptComponent, Entity self, Entity other) {
        if (scriptComponent.hasScript(GameConstants.END_COLLISION)) {
            Script script = scriptComponent.getScript(GameConstants.END_COLLISION);
            script.execute(new CollisionEvent(self, other));
        }
    }

    @Override
    public void preSolve(Contact cntct, Manifold mnfld) {
        return;
    }

    @Override
    public void postSolve(Contact cntct, ContactImpulse ci) {
        return;
    }

}
