/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import rosthouse.rosty.components.DeathComponent;

/**
 *
 * @author Rosthouse
 */
public class CleanUpSystem extends IteratingSystem {

    private Engine engine;

    public CleanUpSystem() {
        super(Family.one(DeathComponent.class).get());
        Gdx.app.log("CLEANUP", "Loading Cleanup System");
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine); //To change body of generated methods, choose Tools | Templates.
        this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float f) {
        engine.removeEntity(entity);
    }

}
