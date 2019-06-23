package game.pong.beta.components;

import game.pong.beta.baseGame.PongGameBeta;

/**
 * @author Paweł Kumanowski
 * Class which handles the the game score and sets setting
 */
public class Score {
    private int sets;
    private int points;

    /**
     * Score class contructor
     * @param sets int
     * @param points int
     */
    public Score(int sets, int points) {
        this.sets = sets;
        this.points = points;
    }

    /**
     * Method for getting sets number
     * @return int
     */
    public int getSets() {
        return this.sets;
    }

    /**
     * Method for setting Sets
     * @param sets int
     */
    public void setSets(int sets) {
        this.sets = sets;
    }

    /**
     * Method for getting points
     * @return points object
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * Method for setting points
     * @param points points object
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Method for adding points
     *
     * @return (returns code of either match or sets)
     */
    public int addOnePoint() {
        this.points += 1;
        int code;
        if (this.points == PongGameBeta.points) {
            this.points = 0;
            code = addOneSet();
        } else if (this.points == PongGameBeta.points - 1) {
            if (PongGameBeta.sets - 1 == this.sets) {
                code = 9;   // match point
            } else {
                code = 2;   // set point
            }
        } else {
            code = 0;
        }

        return code;
    }

    /**
     * Method for adding sets.
     *
     * @return return codes for either ending the game or resetting points for the second player
     */
    private int addOneSet() {
        this.sets += 1;
        if (this.sets == PongGameBeta.sets) {
            return 99;          // code for the end of the game
        } else {
            return 5;           // code for resetting the points for the second player
            // new set
        }
    }
}
