package ro.tpg.jme3.util;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import ro.tpg.jme3.util.context.ContextData;

public class Util {

    public static AudioNode createAudioNode(final String path, final AssetManager assetManager) {

        final AudioNode pianoSoundNode = new AudioNode(assetManager, path, false);
        pianoSoundNode.setPositional(false);
        pianoSoundNode.setLooping(false);
        pianoSoundNode.setVolume(3);

        return pianoSoundNode;
    }

    public static float distanceToSpatial(final Camera cam, final Spatial spatial) {
        return cam.getLocation().distance(spatial.getWorldTranslation());
    }
    
    /**
     * Test if this element is currently visible to the first person agent.
     *
     * @param spatial the spatial you are testing for
     * @param cam you should pass it as a clone
     * @param environment the scene the spatial is part of
     * @return
     */
    public static boolean isOnScreen(final Spatial spatial, final Camera cam, final Node environment) {
        if (spatial == null) {
            return false;
        }

        BoundingVolume bv = spatial.getWorldBound();
        int planeState = cam.getPlaneState();
        cam.setPlaneState(0);
        Camera.FrustumIntersect result = cam.contains(bv);
        cam.setPlaneState(planeState);
        if (result == Camera.FrustumIntersect.Inside || result == Camera.FrustumIntersect.Intersects) {

            final Vector3f direction = spatial.getWorldTranslation().subtract(cam.getLocation());
            final Ray ray = new Ray(cam.getLocation(), direction);

            final CollisionResults collisionResults = new CollisionResults();
            environment.collideWith(ray, collisionResults);

            final CollisionResult closestResult = collisionResults.getClosestCollision();
            if (closestResult != null) {
                Spatial closestGeometry = closestResult.getGeometry();

                while (closestGeometry != null && !closestGeometry.getUserDataKeys().contains(ContextData.TAG)) {
                    closestGeometry = closestGeometry.getParent();
                }

                if (closestGeometry != null) {
                    final ContextData data = closestGeometry.getUserData(ContextData.TAG);
                    return data != null && closestGeometry.equals(spatial);
                }
            }

            return false;
        }

        return false;
    }
}
