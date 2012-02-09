/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package awsomebrain;

import se.citerus.crazysnake.Position;

/**
 *
 * @author Dan
 */
public class MinMaxPosition extends Position {
    float score;
    public MinMaxPosition(int x, int y, float score) {
        super(x, y);
        this.score = score;
    }
    
}
