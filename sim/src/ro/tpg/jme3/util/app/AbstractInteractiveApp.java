package ro.tpg.jme3.util.app;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import ro.tpg.jme3.util.agent.Agent;
import ro.tpg.jme3.util.agent.FirstPersonAgentAppState;
import ro.tpg.jme3.util.notifications.NotificationsStateManager;

public abstract class AbstractInteractiveApp extends SimpleApplication {

    private Node environmentScene;
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private String viewPresentationTemplate;
    private static AbstractInteractiveApp instance;

    public static AbstractInteractiveApp getInstance() {
        return instance;
    }

    public AbstractInteractiveApp() {
        showSettings = true;
    }

    @Override
    public void simpleInitApp() {
        environmentScene = createEnvironmentScene();

        instance = this;

        /**
         * Set up Physics
         */
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        // We re-use the flyby camera for rotation, while positioning is handled by physics
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(100);

        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass zero.
        CollisionShape sceneShape =
                CollisionShapeFactory.createMeshShape(environmentScene);
        landscape = new RigidBodyControl(sceneShape, 0);
        environmentScene.addControl(landscape);

        // We attach the scene and the player to the rootnode and the physics space,
        // to make them appear in the game world.
        rootNode.attachChild(environmentScene);
        bulletAppState.getPhysicsSpace().add(landscape);

        stateManager.attach(new NotificationsStateManager());
        stateManager.attach(new FirstPersonAgentAppState(bulletAppState.getPhysicsSpace(), getAgentConfiguration()));
    }

    public AppSettings getSettings() {
        return settings;
    }

    public Node getEnvironmentScene() {
        return environmentScene;
    }

    public String getViewPresentationTemplate() {
        return viewPresentationTemplate;
    }

    protected abstract Node createEnvironmentScene();

    protected Agent getAgentConfiguration() {
        return new Agent(new Vector3f(0, 0, 0), 15);
    }
}
