package game.pong.beta.components;

import game.pong.beta.BaseActor;
import game.pong.beta.BaseScreen;

public class SettingsScreen extends BaseScreen {

    @Override
    public void initialize() {

        BaseActor background = new BaseActor(0,0,mainStage);
        background.loadTexture("abstract1600x900.jpg");
        background.setSize(mainStage.getWidth(), mainStage.getHeight());
    }

    @Override
    public void update(float dt) {

    }
}
