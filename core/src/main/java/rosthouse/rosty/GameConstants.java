/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty;

/**
 *
 * @author Rosthouse
 */
public class GameConstants {

    public final static String START_COLLISION = "StartCollision";
    public final static String END_COLLISION = "EndCollision";
    public final static String POST_SOLVE = "PostSolve";
    public final static String PRE_SOLVE = "PreSolve";

    //Bit flags
    public static final int REMOVE = 0x00000001;

    public enum EventType {

        EndLevel(1);

        public final int value;

        private EventType(int value) {
            this.value = value;
        }
    }

}
