package edu.up.cs301.shapefitter;

/**
 * Solver: finds fit for a shape; completed solution by Vegdahl.
 *
 * @author **** Dylan Price ****
 * @version **** September ?, 2022 ****
 */
public class MyShapeSolver extends ShapeSolver {

    /**
     * Creates a solver for a particular problem.
     *
     * @param parmShape the shape to fit
     * @param parmWorld the world to fit it into
     * @param acc to send notification messages to
     */
    public MyShapeSolver(boolean[][] parmShape, boolean[][] parmWorld, ShapeSolutionAcceptor acc) {
        // invoke superclass constructor
        super(parmShape, parmWorld, acc);
    }

    /**
     * Solves the problem by finding a fit, if possible. The last call to display tells where
     * the fit is. If there is no fit, no call to display should be made--alternatively, a call to
     * undisplay can be made.
     *
     * Worked on this method with Alexander L., Caleithon P., Brent T. and Cameron N.
     */
    public void solve() {
        // create new boolean 2d array for the original shape array (used at later point)
        boolean[][] original = new boolean[shape.length][shape[0].length];

        // copy shape as its original before iterating through orientations
        copyShape(shape, original);

        // iterate through all 8 orientations
        orientation:
        for (Orientation or : Orientation.values()) {
            shapeOrient(or, shape);
            // iterate through world array, make sure shape does not go out of bounds
            wRow:
            for (int i = 0; i < world.length - shape.length + 1; i++) {
                wCol:
                for (int j = 0; j < world[i].length - shape[0].length + 1; j++) {
                    sRow:
                    // iterate through shape array
                    for (int k = 0; k < shape.length; k++) {
                        sCol:
                        for (int m = 0; m < shape[k].length; m++) {
                            display(i, j, or);
                            if (world[i + k][j + m] && shape[k][m]) {
                                // continue iterating through world array if no solution yet
                                continue wCol;
                            }
                            if ((k == shape.length - 1) && (m == shape[k].length - 1)) {
                                // display solution if there is a match between shape and world
                                display(i, j, or);
                                return;
                            }
                        }
                    }
                }
            }
            // copy the original 2d array back to shape
            copyShape(original, shape);
        }
        // call undisplay method if no solution is found
        undisplay();
    }

    /**
     * shapeOrient
     *
     * This method is used as a helper method to solve().
     */
    public void shapeOrient(Orientation or, boolean[][] userShape) {
        // create new temp 2d array variable to copy original shape to during shape rotation
        boolean[][] temp = new boolean [shape.length][shape[0].length];

        // store original shape into temp before orienting shape
        copyShape(userShape, temp);

        // create 2nd temp variable to store the shape mid-orientation
        boolean[][] temp2 = new boolean[shape.length][shape[0].length];

        // switch statements for each shape orientation, used as a helper to solve()
        switch (or) {
            // shape's original orientation
            case ROTATE_NONE:
                break;

            // clockwise orientation
            case ROTATE_CLOCKWISE:
                rotateCW(temp, shape);
                break;

            // 180 degree clockwise orientation, done by calling rotateCW twice
            case ROTATE_180:
                rotateCW(temp, shape);
                // copy the first clockwise orientation into the temp array
                copyShape(shape, temp2);
                rotateCW(temp2, shape);
                break;

            // counter-clockwise orientation
            case ROTATE_COUNTERCLOCKWISE:
                rotateCCW(temp, shape);
                break;

            // reflecting shape's original orientation from left to right
            case ROTATE_NONE_REV:
                mirrorShape();
                break;

            // rotating the original shape clockwise, then reflect it from left to right
            case ROTATE_CLOCKWISE_REV:
                rotateCW(temp, shape);
                // store clockwise orientation into temp array for mirroring shape
                copyShape(shape, temp2);
                mirrorShape();
                break;

            // rotate the original shape 180 clockwise, then reflect it from left to right
            case ROTATE_180_REV:
                rotateCW(temp, shape);
                // store 90 degree cw orientation into temp array
                copyShape(shape, temp2);
                rotateCW(temp2, shape);
                // store 180 degree cw orientation into temp array before mirroring
                copyShape(shape, temp2);
                mirrorShape();
                break;

            // rotate the original shape 90 ccw, then reflect it from left to right
            case ROTATE_COUNTERCLOCKWISE_REV:
                rotateCCW(temp, shape);
                // store 90 degree ccw orientation into temp array before mirroring
                copyShape(shape, temp2);
                mirrorShape();
                break;

            // default case
            default:
                break;
        }
    }

    /**
     * rotateCW
     *
     * Taking a two-dimensional boolean array, this method rotates the user's shape 90 degrees
     * clockwise.
     *
     * Credits to Alexander L. for helping me with this method.
     */
    public void rotateCW(boolean[][] userShape, boolean[][] rotated) {
        for (int i = 0; i < rotated.length; i++) {
            for (int j = 0; j < rotated[i].length; j++) {
                // give new values to original shape array to exhibit cw shape rotation
                rotated[j][rotated.length - i - 1] = userShape[i][j];
            }
        }
    }

    /**
     * rotateCCW
     *
     * Similar to the rotateCW method, with the exception that this method rotates the shape
     * 90 degrees counter-clockwise.
     *
     * Credits to Alexander L. for helping me with this method.
     */
    public void rotateCCW(boolean[][] userShape, boolean[][] rotated) {
        for (int i = 0; i < rotated.length; i++) {
            for (int j = 0; j < rotated[i].length; i++) {
                // give new values to original shape array to exhibit ccw shape rotation
                rotated[rotated.length - j - 1][i] = userShape[i][j];
            }
        }
    }

    /**
     * mirrorShape
     *
     * Solves the problem by finding a fit, if possible. The last call to display tells where
     * the fit is. If there is no fit, no call to display should be made--alternatively, a call to
     * undisplay can be made.
     *
     * Worked on this method with Alexander L., Caleithon P., Brent T. and Cameron N.
     */
    public void mirrorShape() {
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape.length / 2; j++) {
                // create temp 2d boolean array, set it to shape array
                boolean temp = shape[i][j];

                // mirror shape from left to right using the temp array
                shape[i][j] = shape[i][shape[i].length - j - 1];
                shape[i][shape[i].length - j - 1] = temp;
            }
        }
    }

    /**
     * copyShape
     *
     * Copies the two-dimensional shape created by the user, which is stored in another
     * two-dimensional boolean array. for the solve method
     */
    public void copyShape(boolean[][] shape, boolean[][] copy) {
        // iterate through shape array
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                // set copy 2d array to values of shape array
                copy[i][j] = shape[i][j];
            }
        }
    }

    /**
     * Checks if the shape is well-formed: has at least one square, and has all squares connected.
     *
     * @return whether the shape is well-formed
     */
    public boolean check() {
        return Math.random() < 0.5;
    }

}
