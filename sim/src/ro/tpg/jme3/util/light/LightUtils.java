package ro.tpg.jme3.util.light;

import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class LightUtils {
    
    public static void addSingleLightSource(final Node node) {
        
        DirectionalLight dl1 = new DirectionalLight();
        dl1.setColor(ColorRGBA.White);
        dl1.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        node.addLight(dl1);
    }
    
    public static void addMultipleLightSources(final Node node) {
        
        DirectionalLight dl1 = new DirectionalLight();
        dl1.setColor(ColorRGBA.White);
        dl1.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        node.addLight(dl1);

        DirectionalLight dl2 = new DirectionalLight();
        dl2.setColor(ColorRGBA.White);
        dl2.setDirection(new Vector3f(2.8f, -2.8f, 2.8f).normalizeLocal());
        node.addLight(dl2);

        DirectionalLight dl3 = new DirectionalLight();
        dl3.setColor(ColorRGBA.White);
        dl3.setDirection(new Vector3f(-2.8f, -2.8f, 2.8f).normalizeLocal());
        node.addLight(dl3);
    }
}
