/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.collision.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import rosthouse.rosty.scripting.Event;

/**
 *
 * @author Rosthouse <rosthouse@gmail.com>
 */
public class PostSolveCollisionEvent implements Event {

    public final Contact contact;
    public final ContactImpulse impulse;
    public final Entity self;
    public final Entity other;

    public PostSolveCollisionEvent(Contact contact, ContactImpulse impulse, Entity self, Entity other) {
        this.contact = contact;
        this.impulse = impulse;
        this.self = self;
        this.other = other;
    }

}
