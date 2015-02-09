/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.collision.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import rosthouse.rosty.scripting.Event;

/**
 *
 * @author Rosthouse
 */
public class CollisionEvent implements Event {

    public final Contact contact;
    public final Entity self;
    public final Entity other;

    public CollisionEvent(Contact contact, Entity self, Entity other) {
        this.contact = contact;
        this.self = self;
        this.other = other;
    }

}
