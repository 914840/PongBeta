package game.pong.beta.components;

/**
 * Interface do implementacji HUD.
 * docelowo ma też służyć do wyświetlania pozostałych elementów HUD
 * tzn:
 * - przycisk pausa
 * - scoreboard ( set/points )
 */

public interface HudComponent {

    public float getPlayerScore(Player player);

    public void showScoreboard();

    public void upDateScoreboard();

}
