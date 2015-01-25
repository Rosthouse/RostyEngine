/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.scripting;

/**
 *
 * @author Pädda
 * @param <T> A class of Interface {@link Event}.
 */
public interface Script<T extends Event> {

    public Object execute(T event);
}
