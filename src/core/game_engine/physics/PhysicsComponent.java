package core.game_engine.physics;

import core.game_engine.Component;
import core.game_engine.GameObject;
import core.game_engine.LayerTypes;
import core.game_engine.Sprite;
import processing.core.PVector;

public class PhysicsComponent extends Component {
    private PVector velocity = new PVector(0,0,0);
    public float maxSpeed = 5f;
    private float friction = 0.9f;
    private float spacer = 0.3f;
    private float gravity = 1.1f;
    private BoxCollider2D boxCollider2D;

    private boolean isGrounded = false;


    public PhysicsComponent(GameObject g, BoxCollider2D b){
        super(g);
        this.boxCollider2D = b;

    }

    public boolean isGrounded() {
        return isGrounded;
    }

    @Override
    protected void update() {
        if(this.velocity.mag() > this.maxSpeed){
            float tmpY = this.velocity.y;
            this.velocity.setMag(this.maxSpeed);
            this.velocity.y = tmpY;
        }
        this.velocity.y += gravity;
       if(this.boxCollider2D.otherColliders.size() > 0){
           for(BoxCollider2D b : this.boxCollider2D.otherColliders){
               // move player relative to what it collided with
               if(b.gameObject.getLayerType() == LayerTypes.INTERACTABLE){
                   // add score, remove it
                    b.gameObject.setActive(false);
               }else{
                   // static stuff or moving
                   setCollisionSide(b);
               }

           }
           this.boxCollider2D.otherColliders.clear();
       }
       this.velocity.mult(friction);
       this.gameObject.position = this.gameObject.next_position.copy();
       this.gameObject.next_position.add(this.velocity);
    }
    public void setVelocity(float x, float y){
        this.velocity.x += x;
        this.velocity.y += y;
        if(this.velocity.y < 0){
            this.isGrounded = false;
        }
    }
    private void setCollisionSide(BoxCollider2D otherBox2D){
        this.boxCollider2D.findCollisionSide(otherBox2D);
        Point otherTopRight = otherBox2D.getBounds().getTopRight();
        Point otherBottonLeft = otherBox2D.getBounds().getBottomLeft();
        // switch case for the side hit...
        switch (this.boxCollider2D.getHitSideV()) {
            case BOTTOM:
                if (velocity.y >= 0.0f) {
                    this.gameObject.next_position.y = otherTopRight.getY() - this.boxCollider2D.getBounds().getHeight() / 2f + spacer;
                    velocity.y = 0f;
                    this.isGrounded = true;
                }
                break;
            case TOP:
                // put this object on the bottom
                if (velocity.y < -0.01f) {
                    this.gameObject.next_position.y = otherBottonLeft.getY() + this.boxCollider2D.getBounds().getHeight() / 2f + spacer;
                    velocity.y = gravity;
                }
                break;

            case NONE:
                this.isGrounded = false;
                break;
        }


        switch (this.boxCollider2D.getHitSideH()){

            case LEFT:
               if(velocity.x > 0.01f){  // checking x if moving greater than 0.01f because the float value is never exactly zero
                   this.gameObject.next_position.x = otherBottonLeft.getX() - this.boxCollider2D.getBounds().getWidth() / 2f - spacer;
                   velocity.x = 0f;
               }

                break;
            case RIGHT:
                if(velocity.x < -0.01f){ // checking x if moving left less than 0.01f because the float value is never exactly zero
                    this.gameObject.next_position.x = otherTopRight.getX() + this.boxCollider2D.getBounds().getWidth() / 2f + spacer;
                    velocity.x = 0f;
                }

                break;
            case NONE:
                this.isGrounded = false;
                break;
        }
    }
}
