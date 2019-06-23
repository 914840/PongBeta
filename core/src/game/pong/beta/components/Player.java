package game.pong.beta.components;

/**
 * @author Maciej Tymorek
 * Player class with regular getter and setter methods.
 */
public class Player {
    private String nick;
    private Score score;
    private boolean ai;
    private boolean online;

    /**
     * Player constructor
     * @param nick Player's nickname
     * @param ai is Player human or computer controlled
     * @param online is it single-player or multi-player
     */
    Player(String nick, boolean ai, boolean online) {
        this.nick = nick;
        this.score = new Score(0, 0);
        this.ai = ai;
        this.online = online;
    }

    /**
     * Basic Player constructor
     * @param nick Player's nickname
     */
    public Player(String nick) {
        this.nick = nick;
        this.score = new Score(0, 0);
        this.ai = false;
    }

    /**
     * Getting Player's nickname
     * @return String
     */
    public String getNick() {
        return this.nick;
    }

    /**
     * Setting Player's nickname
     * @param nick String
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * Getting Score setting
     * @return Score object
     */
    public Score getScore() {
        return this.score;
    }

    /**
     * Method checking if Player is human
     * @return boolean
     */
    public boolean isAi() {
        return this.ai;
    }

    /**
     * Method for veryfing online status
     * @return boolean
     */
    public boolean isOnline() {
        return this.online;
    }

}
