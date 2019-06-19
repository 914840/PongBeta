package game.pong.beta.components;

public class Player {
    private String nick;
    private Score score;
    private boolean ai;
    private boolean online;

    Player(String nick, boolean ai, boolean online)
    {
        this.nick = nick;
        this.score = new Score(0,0);
        this.ai = ai;
        this.online = online;
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

    public boolean isOnline() { return this.online; }

    public void setOnline(boolean online){ this.online = online; }
}
