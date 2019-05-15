package game.pong.beta.components;

public class Player {
    private String nick;
    private Score score;
    private boolean ai;

    Player(String nick, boolean ai)
    {
        this.nick = nick;
        this.score = new Score(0,0);
        this.ai = ai;
    }

    Player(String nick)
    {
        this.nick = nick;
        this.score = new Score(0,0);
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

    public Score getScore()
    {
        return this.score;
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
