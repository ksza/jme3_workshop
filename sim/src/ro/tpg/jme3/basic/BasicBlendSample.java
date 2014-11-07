package ro.tpg.jme3.basic;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;
import ro.tpg.jme3.util.light.LightUtils;

public class BasicBlendSample extends SimpleApplication {

    public static void main(String[] args) {
        BasicBlendSample app = new BasicBlendSample();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        final Spatial sceneModel = assetManager.loadModel("Scenes/basicBlend.j3o");
        rootNode.attachChild(sceneModel);
        
        LightUtils.addSingleLightSource(rootNode);
        
        flyCam.setMoveSpeed(50);
        flyCam.setZoomSpeed(30);

        sceneModel.setLocalTranslation(-5.0f, -5.2f, 3f);
        sceneModel.setLocalScale(2);
    }
}
