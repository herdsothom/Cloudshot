package model.being.enemystates;

import model.being.player.AbstractPlayer;
import model.being.enemies.AbstractEnemy;

/**
 * State when the player has entered an enemy's aggro radius
 *
 * This state just moves the enemy to the location of the player in the X
 *
 * Just defines movement doesn't attack.
 * */
public class BossState1 implements EnemyState, java.io.Serializable {


    @Override
    public void update(AbstractEnemy e, AbstractPlayer player) {
        if(e.getX()<player.getX() && Math.abs(e.getX()-player.getX())>1)
            e.getBody().setLinearVelocity(1f,e.getBody().getLinearVelocity().y);
        if(Math.abs(e.getX()-player.getX())>1 &&e.getX()>player.getX())
            e.getBody().setLinearVelocity(-1f,e.getBody().getLinearVelocity().y);
    }

    @Override
    public int attack(AbstractEnemy e, AbstractPlayer p) {
        return 0;
    }

    @Override
    public void damage(AbstractEnemy e, int damage) {
        e.internalDamage(damage);
        if(e.getHealth() <= 0){
            e.enemyState = new Death();
        }
    }

    @Override
    public String toString() {
        return "AggroMovementState";
    }
}
