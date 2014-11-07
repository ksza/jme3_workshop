package ro.tpg.basic;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class BasicBlendSample extends SimpleApplication {

    public static void main(String[] args) {
        BasicBlendSample app = new BasicBlendSample();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        final Spatial sceneModel = assetManager.loadModel("Scenes/basicBlend.j3o");
        rootNode.attachChild(sceneModel);
        
        DirectionalLight dl1 = new DirectionalLight();
        dl1.setColor(ColorRGBA.White);
        dl1.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl1);
        
        flyCam.setMoveSpeed(50);
        flyCam.setZoomSpeed(30);

        sceneModel.setLocalTranslation(-5.0f, -5.2f, 3f);
        sceneModel.setLocalScale(2);

    }
}
