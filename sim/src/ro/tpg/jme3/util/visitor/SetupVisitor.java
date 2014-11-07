package ro.tpg.jme3.util.visitor;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import ro.tpg.jme3.util.app.AbstractInteractiveApp;
import ro.tpg.jme3.util.context.ContextData;

/**
 * Find and initialize spatials in your scene graph
 */
public class SetupVisitor implements SceneGraphVisitor {

    private final AbstractInteractiveApp app;
    
    public SetupVisitor(final SimpleApplication app) {
        this.app = (AbstractInteractiveApp) app;
    }       
    
    public void visit(Spatial spatial) {
        final ContextData data = spatial.getUserData(ContextData.TAG);
        if (data != null) {
            
        }
    }
}
