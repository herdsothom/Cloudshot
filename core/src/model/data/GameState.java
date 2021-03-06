package model.data;

import com.badlogic.gdx.Preferences;
import model.being.enemies.AbstractEnemy;
import model.being.player.AbstractPlayer;

import java.util.List;

/**
 * A wrapper class for a Preference instance. A GameState object is the intermediary between the
 * libgdx.Preferences (which store Strings mapped to primitive values) and the TransactionHandler which
 * opens write/read/contains queries on the GameState object.
 * Created by Dan Ko on 9/19/2017.
 */
public class GameState{
    private Preferences state;


    public GameState(Preferences pref) {
        state = pref;
    }


    public Preferences getPref() {
        return state;
    }

    
    /** puts the bytecode of a PlayerData
     *  into the Preference
     *  @param player as bytecode
     */
    public void setPlayerInPref(String player) {
        state.putString("Player", player);
    }
    
    /** puts the bytecode of a List<AbstractEnemies>
     *  into the Preference
     * @param enemies
     */
    public void setEnemiesInPref(String enemies) {
        state.putString("Enemies", enemies);
    }


    /** puts the bytecode of an AbstractLevel
     * into the Preference
     * @param level
     */
    public void setLevelInPref(String level) {
        state.putString("Level", level);
    }



    /** As the model will load this value in, assure the TransactionHandler
     * that this value exists inside the GameState
     * @return assurance.
     */
    public boolean containsPlayer() {
        return state.contains("Player");
    }

    /** As the model will load this value in, assure the TransactionHandler
     * that this value exists inside the GameState
     * @return assurance.
     */
    public boolean containsEnemies() {
        return state.contains("Enemies");
    }

    public boolean containsLevel() {
        return state.contains("Level");
    }


}
