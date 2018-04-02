import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by fabii on 27.03.2018.
 */

public class Player {
    private static final float SIZE = 1f;
    Vector2 position = new Vector2();
    Rectangle bounds = new Rectangle();


    //Constructor
    public Player(Vector2 pos) {
        this.position = pos;
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
    }


    //Update method
    public void update(float delta) {

    }

    //Getters and setters
    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public static float getSize() {
        return SIZE;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
