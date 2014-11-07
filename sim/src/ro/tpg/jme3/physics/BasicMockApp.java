package ro.tpg.jme3.physics;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

public class BasicMockApp extends SimpleApplication {

    private Node environmentScene; 
    private BulletAppState bulletAppState;
    private RigidBodyControl rigidEnvironment;
    
    public static void main(String[] arg) {
        final SimpleApplication app = new BasicMockApp();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        
        environmentScene = new MockScene(assetManager, cam);
        
        /**
         * Set up Physics
         */
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        /* we re-use the flyby camera for rotation, while positioning is handled by physics */
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(30);

        /*
         * We set up collision detection for the scene by creating a compound collision shape and a static 
         * RigidBodyControl with mass zero.
         */
        CollisionShape sceneShape =
                CollisionShapeFactory.createMeshShape(environmentScene);
        rigidEnvironment = new RigidBodyControl(sceneShape, 0);
        environmentScene.addControl(rigidEnvironment);

        /*
         * We attach the scene and the player to the rootnode and the physics space,
         * to make them appear in the game world.
         */
        rootNode.attachChild(environmentScene);
        bulletAppState.getPhysicsSpace().add(rigidEnvironment);
    }
}
