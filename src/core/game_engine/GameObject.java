package core.game_engine;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public abstract class GameObject {
    public PApplet parent;
    public PVector position;
    public PVector next_position;
    protected LayerTypes layerType = LayerTypes.BACKGROUND;
    public LayerTypes getLayerType() {
        return layerType;
    }

    protected boolean isActive = true;
    public void setActive(boolean active) {
        isActive = active; // use in update to stop drawing or updating an object
    }



    protected ArrayList<Component> componentList = new ArrayList<>();
    public GameObject(){

    }
    public GameObject(PApplet p){
        this.parent = p;
        this.position = new PVector(0,0,0);
        this.next_position = new PVector(0,0,0);
    }
    public abstract void update();
    public void addComponentList(Component c){
        componentList.add(c);
    }
}
