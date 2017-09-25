package model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import model.being.AbstractEnemy;

import model.being.Player;
import model.mapObject.levels.AbstractLevel;

import java.util.ArrayList;
import java.util.List;


public class GameModel {

    Player player;
    List<AbstractEnemy> enemies;
    AbstractLevel level;

    private float elapsedTime = 0f;

    //Box2D
    public static final float PPM = 100;//pixelPerMeter
    World world;
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera cam;
    //End

    public GameModel(AbstractLevel level, OrthographicCamera cam) {
        //Box2D
        this.cam = cam;
        world = new World(new Vector2(0, -100), true);
        debugRenderer = new Box2DDebugRenderer();
        BodyDef groundDef= new BodyDef();
        groundDef.position.set(new Vector2(0,10));
        Body groundBody = world.createBody(groundDef);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(cam.viewportWidth, 10.0f);
        groundBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();
        //ground.
        //End

        this.level = level;
        player = new Player(new Vector2(50,200), 50, 50, 100, 40,world);
        enemies = new ArrayList<>();
        Gdx.input.setInputProcessor(player);
    }

    public void updateState(float elapsedTime){
        this.elapsedTime = elapsedTime;
        updatePlayerModel();
        for(AbstractEnemy ae : enemies){
            ae.update();
        }
        world.step(1/60f,6,2);
    }


    public void addEnemy(AbstractEnemy enemy){
        enemies.add(enemy);
    }

    public void draw(SpriteBatch sb){
        sb.draw(player.getImage().getFrameFromTime(elapsedTime),player.getX(),player.getY());
        for(AbstractEnemy ae : enemies){
            sb.draw(ae.getImage().getFrameFromTime(elapsedTime),ae.getX(),ae.getY());
        }

        //Box2D
        debugRenderer.render(world, cam.combined);
        world.step(1/60f, 6, 2);
        sb.draw(player.getImage().getFrameFromTime(elapsedTime),player.getX(),player.getY());

    }

    private void updatePlayerModel(){
        player.update(level.getTiles());
        for(AbstractEnemy e : enemies){
            player.attack(e);
        }
    }

    public TiledMapRenderer getTiledMapRenderer() {
        return level.getTiledMapRenderer();
    }

    public Player getPlayer() {
        return player;
    }

    public List<AbstractEnemy> getEnemies() {
        return enemies;
    }

    public AbstractLevel getLevel() {
        return level;
    }
}
