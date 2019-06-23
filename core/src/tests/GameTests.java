package tests;

import game.pong.beta.components.Player;
import game.pong.beta.components.Score;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTests {

    @Test
    public void playerTest(){
        Player player = new Player("Aron");
        assertEquals(player.getNick(), "Aron");
    }

    @Test
    public void scoreTest(){
        Score score = new Score(12, 12);
        assertEquals(score.getPoints(), 12);
        assertEquals(score.getSets(), 12);
    }
}
