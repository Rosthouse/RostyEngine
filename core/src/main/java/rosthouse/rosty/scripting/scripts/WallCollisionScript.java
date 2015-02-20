/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.scripting.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import java.util.Arrays;
import rosthouse.rosty.collision.events.PostSolveCollisionEvent;
import rosthouse.rosty.scripting.NativeScript;

/**
 *
 * @author Rosthouse <rosthouse@gmail.com>
 */
public class WallCollisionScript extends NativeScript<PostSolveCollisionEvent> implements Disposable {

    public final Sound hitSound;

    public WallCollisionScript() {
        this.hitSound = Gdx.audio.newSound(Gdx.files.classpath("Audio/Effects/wall_collision.wav"));
    }

    @Override
    public Object execute(PostSolveCollisionEvent event) {

        float[] impulses = event.impulse.getNormalImpulses();
        Gdx.app.debug("IMPULSES", String.format("%s", Arrays.toString(impulses)));
        float impulse = impulses[0];
        if (impulse > 5) {
            float volume = MathUtils.sinDeg(impulse);
            Gdx.app.debug("IMPULSES", String.format("Volume: %.2f", volume));
            hitSound.play(volume);
        }
        return null;
    }

    @Override
    public void dispose() {
        hitSound.dispose();
    }

}
