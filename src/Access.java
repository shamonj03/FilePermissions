public class Access {
	/**
	 * The value for no rights, 0.
	 */
	public static final byte NONE = 0x0;

	/**
	 * The value for execute rights, 1 or 001.
	 */
	public static final byte EXECUTE = 0x1;
	/**
	 * The value for write rights, 2 or 010.
	 */
	public static final byte WRITE = 0x2;
	/**
	 * The value for read rights, 4 or 100.
	 */
	public static final byte READ = 0x4;
	/**
	 * The value for all rights, 7 or 111.
	 */
	public static final byte ALL = READ | WRITE | EXECUTE;

	private Access() {
		// prevent instantiation.
	}
}
