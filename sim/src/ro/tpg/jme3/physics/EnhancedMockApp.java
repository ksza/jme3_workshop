package ro.tpg.jme3.physics;

import ro.tpg.jme3.util.app.AbstractInteractiveApp;
import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import ro.tpg.jme3.util.agent.Agent;

public class EnhancedMockApp extends AbstractInteractiveApp {

    public static void main(String[] args) {
        final Application app = new EnhancedMockApp();
        app.start();
    }
    
    @Override
    protected Node createEnvironmentScene() {
        return new MockScene(assetManager, cam);
    }
    
    @Override
    protected Agent getAgentConfiguration() {
        return new Agent(new Vector3f(0, 10, 30), 15);
    }
}
