/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty;

import com.badlogic.ashley.core.Entity;

/**
 *
 * @author Pädda
 */
public class EntityRemoval {

    public final Entity entityToRemove;

    public EntityRemoval(Entity entityToRemove) {
        this.entityToRemove = entityToRemove;
    }

}
