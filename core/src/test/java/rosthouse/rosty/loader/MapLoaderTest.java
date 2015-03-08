/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rosthouse.rosty.loader;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.assets.AssetManager;
import java.util.Collection;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import rosthouse.rosty.systems.PhysicsSystem;

/**
 *
 * @author Rosthouse <rosthouse@gmail.com>
 */
public class MapLoaderTest {
    MapLoader instance;
    
    public MapLoaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance = new MapLoader();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of loadMap method, of class MapLoader.
     */
    

    /**
     * Test of getScriptName method, of class MapLoader.
     */
    @Test
    public void testGetScriptNameWithoutParameters() {
        assertThat(instance.getScriptName("EndLevelScript"), equalTo("EndLevelScript"));
    }
    /**
     * Test of getScriptName method, of class MapLoader.
     */
    @Test
    public void testGetScriptNameWithParameters() {
        assertThat(instance.getScriptName("EndLevelScript(Hello,World)"), equalTo("EndLevelScript"));
    }
    
    @Test
    public void testGetScriptNameWithParametersSpaced() {
        assertThat(instance.getScriptName("EndLevelScript( Hello, World)"), equalTo("EndLevelScript"));
    }
    
    @Test
    public void testGetParameters() {
        Collection parameters = instance.getScriptParameters("EndLevelScript( Hello, World)");
        assertThat(parameters, contains(hasToString("Hello"), hasToString("World")));
    }
    
    @Test
    public void testGetParametersMoreThan2() {
        Collection parameters = instance.getScriptParameters("EndLevelScript( Hello, Beautiful, World)");
        assertThat(parameters, contains("Hello","Beautiful", "World"));
    }
    
}
