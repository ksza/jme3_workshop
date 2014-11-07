package ro.tpg.jme3.util.control;

import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import ro.tpg.jme3.util.Util;

public class ProximitySensor extends AbstractControl {

    private Camera camera;
    
    public ProximitySensor(Camera camera) {
        this.camera = camera;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        
        final float distance = Util.distanceToSpatial(camera, spatial);
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
