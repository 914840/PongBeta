package game.pong.beta.components;

public interface HudComponent {

    public float getPlayerScore(Player player);

    public void showScoreboard();

    public void upDateScoreboard();

}
