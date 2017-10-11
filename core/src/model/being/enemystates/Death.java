package model.being.enemystates;

import model.being.player.AbstractPlayer;
import model.being.enemies.AbstractEnemy;

public class Death implements EnemyState, java.io.Serializable {
    @Override
    public void update(AbstractEnemy e, AbstractPlayer p) {
        e.getWorld().destroyBody(e.getBody());
    }

    @Override
    public int attack(AbstractEnemy e, AbstractPlayer p) {
        return 0;
    }

    @Override
    public void damage(AbstractEnemy e, int damage) {
        //dont do anything, bec dead.
    }

    @Override
    public String toString() {
        return "DeathState";
    }
}