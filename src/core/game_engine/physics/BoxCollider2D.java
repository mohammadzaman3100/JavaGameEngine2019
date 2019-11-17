package core.game_engine.physics;

import core.game_engine.Component;
import core.game_engine.Sprite;

import java.util.ArrayList;

public class BoxCollider2D extends Component {
    // bounds rectangle
    private Rectangle bounds;
    private boolean hasCollided = false;
    private SIDES hitSideV = SIDES.NONE;
    // for sideways and vertical collisions there is a need for a new SIDE
    private SIDES hitSideH = SIDES.NONE;
    // private SIDES hitSide = SIDES.NONE; this is now hitSideH
    public boolean mouse_over = false; // flag for mouse interaction

    public SIDES getHitSideV() {
        return hitSideV;
    }

    public SIDES getHitSideH() {
        return hitSideH;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public ArrayList<BoxCollider2D> otherColliders = new ArrayList<>();

    public BoxCollider2D(Sprite g, float w, float h) {
        super(g);
        this.bounds = new Rectangle(gameObject.position.x, gameObject.position.y, w, h);
    }

    @Override
    protected void update() {

        this.bounds.updateBounds(gameObject.position.x, gameObject.position.y);
        this.mouse_over = this.bounds.pointHit(this.gameObject.parent.mouseX, this.gameObject.parent.mouseY);
    }

    public void check_collisions(BoxCollider2D other) {
        hasCollided = bounds.isOverLapping(other.bounds);
        if (hasCollided) {
            this.otherColliders.add(other);
        }
    }

    public void findCollisionSide(BoxCollider2D otherBox2D) {
        // over lap has been confirmed, now detect the actual side
        hitSideV = SIDES.NONE;
        // is touching above
        boolean isTouchingAbove = this.bounds.getIsTouchingAbove(otherBox2D.getBounds());
        boolean isTouchingBelow = false;
        if (!isTouchingAbove) {
            // check below!
            isTouchingBelow = this.bounds.getIsTouchingBelow(otherBox2D.getBounds());
        }
        if (isTouchingAbove) {
            hitSideV = SIDES.BOTTOM;
        } else if (isTouchingBelow) {
            hitSideV = SIDES.TOP;
        }
        hitSideH = SIDES.NONE;
        // check proximity to a platform below
        float distanceAbove = Math.abs(otherBox2D.getBounds().getTopRight().getY() - this.bounds.getBottomLeft().getY());
        if(distanceAbove < 1f){
            return;
        }
        boolean isTouchingRight = this.bounds.getIsTouchingRight(otherBox2D.getBounds());
        boolean isTouchingLeft = false;

        if (!isTouchingRight) {
            isTouchingLeft = this.bounds.getIsTouchingLeft(otherBox2D.getBounds());
        }
        if (isTouchingLeft) {
            hitSideH = SIDES.LEFT;
        } else if (isTouchingRight) {
            hitSideH = SIDES.RIGHT;
        }
    }


}
