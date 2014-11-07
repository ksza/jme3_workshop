package ro.tpg.jme3.util.agent;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import ro.tpg.jme3.util.context.ContextData;
import ro.tpg.jme3.util.notifications.NotificationsStateManager;
import ro.tpg.jme3.util.app.AbstractInteractiveApp;

/**
 *
 * @author kszanto
 */
public class FirstPersonAgentAppState extends AbstractAppState implements ActionListener {

    private CharacterControl characterControl;
    private boolean left = false, right = false, up = false, down = false;
    private Vector3f agentWalkDirection = new Vector3f();

    /* Temporary vectors used on each frame. 
     * They here to avoid instanciating new vectors on each frame!
     */
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private Node rootNode;
    private AbstractInteractiveApp app;
    private Camera cam;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private final PhysicsSpace physicsSpace;
    private Node environment;
    private Node inventory;
    private Vector3f oldPosition = null;
    private Vector3f oldScale = null;
    private final Agent agent;

    public FirstPersonAgentAppState(final PhysicsSpace physicsSpace, final Agent agent) {
        this.physicsSpace = physicsSpace;
        this.agent = agent;
    }

    /**
     * We set up collision detection for the player by creating a capsule
     * collision shape and a CharacterControl. The CharacterControl offers extra
     * settings for size, stepheight, jumping, falling, and gravity. We also put
     * the player in its starting position.
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app); //To change body of generated methods, choose Tools | Templates.

        this.app = (AbstractInteractiveApp) app;
        this.environment = this.app.getEnvironmentScene();
        this.cam = app.getCamera();
        this.stateManager = stateManager;
        this.assetManager = app.getAssetManager();
        this.rootNode = this.app.getRootNode();

        inventory = new Node("Inventory");
        DirectionalLight dl1 = new DirectionalLight();
        dl1.setColor(ColorRGBA.White);
        dl1.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        inventory.addLight(dl1);
        this.app.getGuiNode().attachChild(inventory);

        characterControl = new CharacterControl(new CapsuleCollisionShape(1.5f, agent.getAgentHeight(), 1), 0.05f);

        characterControl.setJumpSpeed(20);
        characterControl.setFallSpeed(30);
        characterControl.setGravity(70);
        characterControl.setPhysicsLocation(agent.getInitialAgentPosition());

        physicsSpace.add(characterControl);

        initKeys(app.getInputManager());
        initCrossHairs(); // a "+" in the middle of the screen to help aiming
    }

    /**
     * A centred plus sign to help the player aim.
     */
    protected void initCrossHairs() {
        app.setDisplayStatView(false);
        final BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(font, false);
        ch.setSize(font.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation( // center
                app.getSettings().getWidth() / 2 - ch.getLineWidth() / 2, app.getSettings().getHeight() / 2 + ch.getLineHeight() / 2, 0);
        app.getGuiNode().attachChild(ch);
    }

    /**
     * Initialize the keys used to control this agent
     *
     * @param inputManager the apps input manager instance
     */
    private void initKeys(final InputManager inputManager) {

        inputManager.addMapping("Interact",
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // left-button click
        inputManager.addListener(interactListener, "Interact");

        inputManager.addMapping("PutBack",
                new MouseButtonTrigger(MouseInput.BUTTON_RIGHT)); // right-button click
        inputManager.addListener(putBackListener, "PutBack");

        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
    }

    /**
     * These are our custom actions triggered by key presses. We do not walk
     * yet, we just keep track of the direction the user pressed.
     */
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Left")) {
            left = isPressed;
        } else if (name.equals("Right")) {
            right = isPressed;
        } else if (name.equals("Up")) {
            up = isPressed;
        } else if (name.equals("Down")) {
            down = isPressed;
        } else if (name.equals("Jump")) {
            if (isPressed) {
                characterControl.jump();
            }
        }
    }

    /**
     * Should be called from application's simple update method! Actual walking
     * happens here. We check in which direction the player is walking by
     * interpreting the camera direction forward (camDir) and to the side
     * (camLeft). The setWalkDirection() command is what lets a
     * physics-controlled player walk. We also make sure here that the camera
     * moves with player.
     */
    @Override
    public void update(float tpf) {
        super.update(tpf);

        camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        agentWalkDirection.set(0, 0, 0);
        if (left) {
            agentWalkDirection.addLocal(camLeft);
        }
        if (right) {
            agentWalkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            agentWalkDirection.addLocal(camDir);
        }
        if (down) {
            agentWalkDirection.addLocal(camDir.negate());
        }
        characterControl.setWalkDirection(agentWalkDirection);
        cam.setLocation(characterControl.getPhysicsLocation());
    }
    private ActionListener interactListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Interact") && !keyPressed) {

                if (!inventory.getChildren().isEmpty()) {

                    CollisionResults results = new CollisionResults();
                    Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                    environment.collideWith(ray, results);

                    if (results.size() > 0) {
                        CollisionResult closest = results.getClosestCollision();
                        Spatial s = closest.getGeometry();

                        while (!s.getUserDataKeys().contains(ContextData.TAG) && !s.equals(environment)) {
                            s = s.getParent();
                        }

                        final Spatial s1 = inventory.getChild(0);
                        final ContextData s1Data = s1.getUserData(ContextData.TAG);

                        final ContextData data = s.getUserData(ContextData.TAG);
                        /* take into consideration only objects having contextual data */
                        if (data != null && !s.equals(environment)) {

                            stateManager.getState(NotificationsStateManager.class).addNotification("(Interact using " + s1Data.getId() + ") with " + data.getId());
                        } else {
                            stateManager.getState(NotificationsStateManager.class).addNotification("(Interact using " + s1Data.getId() + ") with other entities, not implemented!");
                        }
                    }
                } else {
                    CollisionResults results = new CollisionResults();
                    Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                    environment.collideWith(ray, results);

                    if (results.size() > 0) {
                        CollisionResult closest = results.getClosestCollision();
                        Spatial s = closest.getGeometry();

                        while (!s.getUserDataKeys().contains(ContextData.TAG) && !s.equals(environment)) {
                            s = s.getParent();
                        }

                        final ContextData data = s.getUserData(ContextData.TAG);

                        /* take into consideration only objects having contextual data */
                        if (data != null && !s.equals(environment)) {

                            if (data.isPickable()) {
                                pickObjectUp(s, data);
                            } else {
                                stateManager.getState(NotificationsStateManager.class).addNotification("(Interact) " + data.getId() + ", cannot be picked up");
                            }
                        }
                    }
                }
            }
        }
    };
    private ActionListener putBackListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("PutBack") && !keyPressed) {

                if (!inventory.getChildren().isEmpty()) {

                    Spatial s1 = inventory.getChild(0);
                    // scale back
                    s1.setLocalScale(oldScale);
                    s1.setLocalTranslation(oldPosition);
                    inventory.detachAllChildren();
                    environment.attachChild(s1);
                }
            }
        }
    };

    private void pickObjectUp(final Spatial object, final ContextData data) {

        oldPosition = object.getWorldTranslation().clone();
        oldScale = object.getWorldScale().clone();
        object.getWorldScale();
        environment.detachChild(object);
        inventory.attachChild(object);
        // make it bigger to see on the HUD
        object.scale(50f);
        // make it on the HUD center
        object.setLocalTranslation(app.getSettings().getWidth() / 2, app.getSettings().getHeight() / 2, 0);
    }
}
