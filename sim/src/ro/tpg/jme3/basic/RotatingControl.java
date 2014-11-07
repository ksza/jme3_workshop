package ro.tpg.jme3.basic;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class RotatingControl extends AbstractControl {

    private float speed = 1;
    
    @Override
    protected void controlUpdate(float tpf) {
        spatial.rotate(tpf * speed, 0, 0);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    /**
     * Usefull when you add the control from the SceneExplorer
     * 
     * @param spatial
     * @return 
     */
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        final RotatingControl control = new RotatingControl();
        control.setSpeed(speed);
        control.setSpatial(spatial);
        return control;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }        
}
