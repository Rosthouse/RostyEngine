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
public enum MapObjects {

    FIRE("fire"),
    TYPE("type");

    private MapObjects(String value) {
        this.value = value;
    }

    private final String value;

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

}
