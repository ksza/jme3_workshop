package ro.tpg.jme3.util.light;

import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class LightedNode extends Node {
    
    public LightedNode(final String sceneName) {
        
        super(sceneName);
        
        DirectionalLight dl1 = new DirectionalLight();
        dl1.setColor(ColorRGBA.White);
        dl1.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        this.addLight(dl1);
    }
}
