package data;

public class GameVars {
	public static int WIDTH = 800;
	public static int HEIGHT = 480;
	public static float PPM = 22;	
	public static final short BIT_PACMAN = 2;
	public static final short BIT_GHOST = 4;
	public static final short BIT_PAC = 8;
	public static final short BIT_ENERGIZER = 16;
	public static final short BIT_MAZE = 32;
	public static final short BIT_HOME = 64;

	public enum Direction {		
		left, up, right, down, stoped;	    

	    private Direction opposite;

	    static {
	        left.opposite = right;
	        up.opposite = down;
	        right.opposite = left;
	        down.opposite = up;
	        stoped.opposite = stoped;
	    }

	    public Direction getOppositeDirection() {
	        return opposite;
	    }
	}	
}
