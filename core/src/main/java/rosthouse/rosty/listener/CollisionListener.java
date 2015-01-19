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
import rosthouse.rosty.components.FireComponent;
import rosthouse.rosty.components.WaterComponent;
import rosthouse.rosty.components.shader.ShaderComponent;
import rosthouse.rosty.entities.MovingPicture;

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
        
        if(entityA instanceof MovingPicture){
            checkTile(entityB, entityA);
        } else {
            checkTile(entityA, entityB);
        }
    }

    private void checkTile(Entity tileEntity, Entity marbleEntity) {
        if(tileEntity.getComponent(FireComponent.class) != null){
            ShaderComponent t;
            if((t = tileEntity.getComponent(ShaderComponent.class)) != null){
                marbleEntity.remove(ShaderComponent.class);
                t.dispose();
            }
            marbleEntity.add(new ShaderComponent("shaders/fire")); 
        }else if(tileEntity.getComponent(WaterComponent.class) != null){
            ShaderComponent t;
            if((t = tileEntity.getComponent(ShaderComponent.class)) != null){
                marbleEntity.remove(ShaderComponent.class);
                t.dispose();
            }
            marbleEntity.add(new ShaderComponent("shaders/water"));
        } 
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
