package ro.tpg.jme3.util.agent;

import com.jme3.math.Vector3f;

public class Agent {

    /**
     * The eyesight height
     */
    private final Vector3f initialAgentPosition;
    private final float agentHeight;

    public Agent(Vector3f initialAgentPosition, float agentHeight) {
        this.initialAgentPosition = initialAgentPosition;
        this.agentHeight = agentHeight;
    }

    public Vector3f getInitialAgentPosition() {
        return initialAgentPosition;
    }

    public float getAgentHeight() {
        return agentHeight;
    }
}
