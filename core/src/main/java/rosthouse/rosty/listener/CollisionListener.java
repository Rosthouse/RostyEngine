/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.listener;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import rosthouse.rosty.components.ShaderComponent;

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
        ImmutableArray components = entityA.getComponents();
        entityA.add(new ShaderComponent("shaders/basic"));
    }

    @Override
    public void endContact(Contact cntct) {

        Long idA = (Long) cntct.getFixtureA().getUserData();
        Long idB = (Long) cntct.getFixtureB().getUserData();
        Entity entityA = engine.getEntity(idA);
        ShaderComponent shader = entityA.getComponent(ShaderComponent.class);
        if (shader != null) {
            entityA.remove(ShaderComponent.class);
            shader.shader.dispose();
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
