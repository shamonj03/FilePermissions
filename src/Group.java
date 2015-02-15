public class Group {
	/**
	 * The value for owner group, 1 or 001.
	 */
	public static final byte OWNER = 0x1;
	/**
	 * The value for group group, 2 or 010.
	 */
	public static final byte GROUP = 0x2;
	/**
	 * The value for others group, 4 or 100.
	 */
	public static final byte OTHERS = 0x4;
	/**
	 * The value for all groups, 7 or 111;
	 */
	public static final byte ALL = OWNER | GROUP | OTHERS;

	
	private Group() {
		// prevent instantiation.
	}
}
