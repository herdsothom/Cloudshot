package model.mapObject.terrain;

import com.badlogic.gdx.math.Rectangle;
import view.MovingSprite;
import view.StaticSprite;

/**
 * AbstractTerrain implementation.
 * The difference between a platform and ground is that platform is hovering in the air.
 */
public class Platform extends AbstractTerrain {

    private Rectangle platform;

    public Platform(int x, int y, int width, int height){
        platform = new Rectangle(x,y,width,height);
    }

    @Override
    public int getX() {
        return (int)platform.getX();
    }

    @Override
    public int getY() {
        return (int)platform.getY();
    }

    @Override
    public Rectangle getBoundingbox() {
        return platform;
    }

    @Override
    public StaticSprite getImage(){
        return new StaticSprite("platform.png",(int)platform.getWidth(),(int)platform.getHeight());

    }

}
