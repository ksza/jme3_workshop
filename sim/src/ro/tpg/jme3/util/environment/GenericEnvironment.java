package ro.tpg.jme3.util.environment;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import ro.tpg.jme3.util.light.LightUtils;
import ro.tpg.jme3.util.light.MultiLightedNode;

public class GenericEnvironment extends MultiLightedNode {

    private final AssetManager assetManager;

    public GenericEnvironment(final String scenePath, final String sceneName, final AssetManager assetManager) {

        super(sceneName);

        this.assetManager = assetManager;
        final Spatial sceneModel = assetManager.loadModel(scenePath);
        sceneModel.setLocalTranslation(0, -5.2f, 0);
        sceneModel.setLocalScale(2);

        this.attachChild(sceneModel);
    }
}
