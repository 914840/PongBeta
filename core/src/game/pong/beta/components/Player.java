package game.pong.beta.components;

public class Player {
    private String nick;
    private float score;
    private boolean ai;

    Player(String nick, float score, boolean ai)
    {
        this.nick = nick;
        this.score = score;
        this.ai = ai;
    }

    Player(String nick)
    {
        this.nick = nick;
        this.score = 0;
        this.ai = false;
    }

    public String getNick()
    {
        return this.nick;
    }

    public void setNick(String nick)
    {
        this.nick = nick;
    }

    public float getScore()
    {
        return this.score;
    }

    public void setScore(float score)
    {
        this.score = score;
    }

    public boolean isAi()
    {
        return this.ai;
    }

    public void setAi(boolean ai)
    {
        this.ai = ai;
    }
}
