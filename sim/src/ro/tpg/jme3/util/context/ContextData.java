package ro.tpg.jme3.util.context;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import java.io.IOException;
import java.util.UUID;

public class ContextData implements Savable {

    public static final String TAG = "CONTEXT_DATA";
    private static final String ID_SAVE_TAG = "ID";
    private static final String PICKABLE_TAG = "PICKABLE";

    private String id = UUID.randomUUID().toString();
    private boolean pickable;

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public boolean isPickable() {
        return pickable;
    }
    public void setPickable(boolean pickable) {
        this.pickable = pickable;
    }
    
    public void write(JmeExporter ex) throws IOException {
        final OutputCapsule capsule = ex.getCapsule(this);
        
        capsule.write(id, ID_SAVE_TAG, "no_id");
        capsule.write(pickable, PICKABLE_TAG, false);
    }

    public void read(JmeImporter im) throws IOException {
        final InputCapsule ic = im.getCapsule(this);
        
        id = ic.readString(ID_SAVE_TAG, UUID.randomUUID().toString());
        pickable = ic.readBoolean(PICKABLE_TAG, false);
    }
    
}
