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
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of loadMap method, of class MapLoader.
     */
    @Test
    @Ignore
    public void testLoadMap() {
        System.out.println("loadMap");
        String path = "";
        AssetManager assetManager = null;
        Engine engine = null;
        PhysicsSystem physicsSystem = null;
        float unitScale = 0.0F;
        MapLoader instance = new MapLoader();
        instance.loadMap(path, assetManager, engine, physicsSystem, unitScale);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getScriptName method, of class MapLoader.
     */
    @Test
    public void testGetScriptNameWithoutParameters() {
        MapLoader instance = new MapLoader();
        assertThat(instance.getScriptName("EndLevelScript"), equalTo("EndLevelScript"));
    }
    /**
     * Test of getScriptName method, of class MapLoader.
     */
    @Test
    public void testGetScriptNameWithParameters() {
        MapLoader instance = new MapLoader();
        assertThat(instance.getScriptName("EndLevelScript(Hello,World)"), equalTo("EndLevelScript"));
    }
    
    @Test
    public void testGetScriptNameWithParametersSpaced() {
        MapLoader instance = new MapLoader();
        assertThat(instance.getScriptName("EndLevelScript( Hello, World)"), equalTo("EndLevelScript"));
    }
    
    @Test
    public void testGetParameters() {
        MapLoader instance = new MapLoader();
        Collection<String> parameters = instance.getScriptParameters("EndLevelScript( Hello, World)");
        assertThat(parameters, hasItems("Hello", "World"));
    }
    
}
