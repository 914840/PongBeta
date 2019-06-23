package game.pong.beta.components;
/**
 * @Author: Paweł Kumanowski
 * @Project: Pong
 * Class which handles the the game score and sets setting
 */
import game.pong.beta.baseGame.PongGameBeta;

public class Score {
    private int sets;
    private int points;

    public Score(int sets, int points){
        this.sets = sets;
        this.points = points;
    }

    public int getSets()
    {
        return this.sets;
    }
    public void setSets(int sets)
    {
        this.sets = sets;
    }

    public int getPoints()
    {
        return this.points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    /**
     * Method for adding points
     * @return
     */
    public int addOnePoint(){
        this.points += 1;
        int code;
        if (this.points == PongGameBeta.points){
            this.points = 0;
             code = addOneSet();
        }
        else if(this.points == PongGameBeta.points - 1)
        {
            if(PongGameBeta.sets - 1 == this.sets)
            {
                code = 9;   // match point
            }
            else
            {
                code = 2;   // set point
            }
        }
        else
        {
             code = 0;
        }

        return code;
    }

    /**
     * Method for adding sets.
     * @return
     */
    private int addOneSet() {
        this.sets += 1;
        if(this.sets == PongGameBeta.sets){
            return 99;          // code for the end of the game
        }
        else {
            return 5;           // code for resseting the points for the second player
                                // new set
        }
    }
}
