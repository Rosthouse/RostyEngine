/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.listener;

import com.badlogic.ashley.core.Entity;
import rosthouse.rosty.scripting.Event;

/**
 *
 * @author Pädda
 */
public class CollisionEvent implements Event {

    public final Entity self;
    public final Entity other;

    public CollisionEvent(Entity self, Entity other) {
        this.self = self;
        this.other = other;
    }

}
