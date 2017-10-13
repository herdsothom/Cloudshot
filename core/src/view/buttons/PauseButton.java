package view.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import view.CloudShotGame;
import view.screens.GameScreen;

public class PauseButton extends ButtonFactory {

    public PauseButton(float x, float y){
        super(x, y);
    }

    @Override
    public TextButton createButton() {
        TextButton pauseButton = new TextButton("Pause", CloudShotGame.gameSkin);
        pauseButton.setWidth(Gdx.graphics.getWidth()/8);
        pauseButton.setPosition(
                x - pauseButton.getWidth()*5,
                y

        );
        pauseButton.addListener(new InputListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                switch(GameScreen.state) {
                    case GAME_RUNNING:
                        GameScreen.state = GameScreen.State.GAME_PAUSED;
                        pauseButton.setText("Resume");
                        break;
                    case GAME_PAUSED:
                        GameScreen.state = GameScreen.State.GAME_RUNNING;
                        pauseButton.setText("Pause");
                        break;
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        return pauseButton;
    }
}
