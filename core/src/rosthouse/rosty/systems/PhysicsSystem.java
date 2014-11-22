/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import java.util.logging.Level;
import java.util.logging.Logger;
import rosthouse.rosty.components.PhysicsComponent;

/**
 *
 * @author Patrick
 */
public class PhysicsSystem extends EntitySystem {

    World world = new World(new Vector2(0, 0), true);

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime); //To change body of generated methods, choose Tools | Templates.
        world.step(deltaTime, 6, 2);
    }

    public <T extends Shape> PhysicsComponent<T> createPhysicsComponent(BodyDef.BodyType type, Class<T> clazz) {
        try {
//            world.
            T t = clazz.newInstance();
            return new PhysicsComponent<T>(t);
        } catch (InstantiationException ex) {
            Logger.getLogger(PhysicsSystem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PhysicsSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
