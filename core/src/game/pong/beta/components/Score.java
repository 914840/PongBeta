package game.pong.beta.components;
/**
 * @Author: Paweł Kumanowski
 * @Project: Pong
 *
 */
import game.pong.beta.PongGameBeta;

public class Score {
    private int sets;
    private int points;

    Score(int sets, int points){
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

    private int addOneSet() {
        this.sets += 1;
        if(this.sets == PongGameBeta.sets){
            return 99;          // kod konca rozgrywki.
        }
        else {
            return 5;           // kod resetu punktów u drugiego gracza
                                // nowy set
        }
    }
}
