package it.polimi.ingsw;

/**
 *
 */
public class GameBoard2Player extends GameBoard{
    /**
     *
     */
    public GameBoard2Player(){
        super();
    }
    /**
     *
     */
    @Override
    protected int[][] createValidPositions(){
        return new int[][]
                {
                        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                        { 0, 0, 0, 1, 1, 0, 0, 0, 0 },
                        { 0, 0, 0, 1, 1, 1, 0, 0, 0 },
                        { 0, 0, 1, 1, 1, 1, 1, 1, 0 },
                        { 0, 1, 1, 1, 1, 1, 1, 1, 0 },
                        { 0, 1, 1, 1, 1, 1, 1, 0, 0 },
                        { 0, 0, 0, 1, 1, 1, 0, 0, 0 },
                        { 0, 0, 0, 0, 1, 1, 0, 0, 0 },
                        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                };
    }

    /**
     *
     */
    @Override
    protected void fillPointStack() {
        for(CommonObjective co: super.commonObjectives){
            co.push(4);
            co.push(8);
        }
    }

    /**
     *
     */
    @Override
    protected int[][] createInvalidPositions() {
        return new int[][]
                {
                        { 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                        { 1, 1, 1, 0, 0, 1, 1, 1, 1 },
                        { 1, 1, 1, 0, 0, 0, 1, 1, 1 },
                        { 1, 1, 0, 0, 0, 0, 0, 0, 1 },
                        { 1, 0, 0, 0, 0, 0, 0, 0, 1 },
                        { 1, 0, 0, 0, 0, 0, 0, 1, 1 },
                        { 1, 1, 1, 0, 0, 0, 1, 1, 1 },
                        { 1, 1, 1, 1, 0, 0, 1, 1, 1 },
                        { 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                };
    }
}
