package ro.tpg.jme3.util.notifications;

import java.util.UUID;


public class Notification {
 
    private final String id;
    private final String message;

    public Notification(final String message) {
        this.id = UUID.randomUUID().toString();
        this.message = message;        
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }       

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof Notification)) {
            return false;
        }
        
        if(obj == null) {
            return false;
        }
        
        if(id.equals(((Notification)obj).id)) {
            return true;
        }
        
        return false;
    }    
}
