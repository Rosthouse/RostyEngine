/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.scripting;

/**
 *
 * @author Pädda
 */
public abstract class NativeScript<T extends Event> implements Script<T> {

    @Override
    public abstract Object execute(T event);

}
