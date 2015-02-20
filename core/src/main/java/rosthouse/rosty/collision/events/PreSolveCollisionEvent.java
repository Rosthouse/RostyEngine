/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.collision.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;
import rosthouse.rosty.scripting.Event;

/**
 *
 * @author Rosthouse <rosthouse@gmail.com>
 */
public class PreSolveCollisionEvent implements Event {

    public PreSolveCollisionEvent(Contact contact, Manifold manifold, Entity self, Entity other) {
    }

}
