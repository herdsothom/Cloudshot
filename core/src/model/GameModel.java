package model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import model.being.AbstractEnemy;

import model.being.Player;
import model.collectable.AbstractCollectable;
import model.data.GameStateTransactionHandler;
import model.data.StateQuery;
import model.mapObject.levels.AbstractLevel;

import java.util.ArrayList;
import java.util.List;


public class GameModel {

    Player player;
    List<AbstractEnemy> enemies;
    AbstractLevel level;
    private GameStateTransactionHandler repoScraper;

    private float elapsedTime = 0f;

    //Box2D
    //public static final float PPM = 100;//pixelPerMeter
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
        groundDef.position.set(new Vector2(0,50));
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

        //generateLevel();

        repoScraper = new GameStateTransactionHandler();
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
        for(AbstractCollectable ac : level.getCollectables()){
            sb.draw(ac.getImage().getFrameFromTime(elapsedTime),ac.getX(),ac.getY(),ac.getBoundingBox().getWidth(),ac.getBoundingBox().getHeight());
        }
        //Box2D
        //debugRenderer.render(world, cam.combined);
        world.step(1/60f, 6, 2);

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


    public void save() {
        if(!repoScraper.save(this)) {

        }
    }

    public void load() {
        try {
            StateQuery loader = repoScraper.load();
            //player = loader.getPlayer();
            //enemies = loader.getEnemies();
        } catch (GameStateTransactionHandler.InvalidTransactionException e) {
            //TODO: SAY TRY AGAIN BUB, FAILED LOAD
        }
    }

    public AbstractLevel getLevel() {
        return level;
    }
}
