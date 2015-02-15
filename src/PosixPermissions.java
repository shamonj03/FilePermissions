import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

public class PosixPermissions {
	/**
	 * Checks if the system is POSIX compatible.
	 */
	private static final boolean POSIX_COMPATIBLE = FileSystems.getDefault()
			.supportedFileAttributeViews().contains("posix");

	/**
	 * The access rights in a compressed integer form.
	 */
	private int access = Access.NONE;

	/**
	 * The file to set permissions for.
	 */
	private File file;

	/**
	 * Create new permissions for a file.
	 * 
	 * @param file
	 *            The file to set permissions for.
	 */
	public PosixPermissions(File file) {
		this.file = file;
	}

	/**
	 * Sets the permissions for the file.
	 * 
	 * Compares the access rights with a binary and.
	 * 
	 * @throws IOException
	 */
	public void updateFilePermissions() {
		try {
			if (POSIX_COMPATIBLE) {
				PosixFileAttributes attributes = Files.getFileAttributeView(
						Paths.get(file.toURI()), PosixFileAttributeView.class)
						.readAttributes();

				Set<PosixFilePermission> permissions = attributes.permissions();

				/**
				 * Set owner access.
				 */
				if ((getOwnerAccess() & Access.READ) == Access.READ) {
					permissions.add(PosixFilePermission.OWNER_READ);
				} else {
					permissions.remove(PosixFilePermission.OWNER_READ);
				}

				if ((getOwnerAccess() & Access.WRITE) == Access.WRITE) {
					permissions.add(PosixFilePermission.OWNER_WRITE);
				} else {
					permissions.remove(PosixFilePermission.OWNER_WRITE);
				}

				if ((getOwnerAccess() & Access.EXECUTE) == Access.EXECUTE) {
					permissions.add(PosixFilePermission.OWNER_EXECUTE);
				} else {
					permissions.remove(PosixFilePermission.OWNER_EXECUTE);
				}

				/**
				 * Set group access.
				 */
				if ((getGroupAccess() & Access.READ) == Access.READ) {
					permissions.add(PosixFilePermission.GROUP_READ);
				} else {
					permissions.remove(PosixFilePermission.GROUP_READ);
				}

				if ((getGroupAccess() & Access.WRITE) == Access.WRITE) {
					permissions.add(PosixFilePermission.GROUP_WRITE);
				} else {
					permissions.remove(PosixFilePermission.GROUP_WRITE);
				}

				if ((getGroupAccess() & Access.EXECUTE) == Access.EXECUTE) {
					permissions.add(PosixFilePermission.GROUP_EXECUTE);
				} else {
					permissions.remove(PosixFilePermission.GROUP_EXECUTE);
				}

				/**
				 * Set others access.
				 */
				if ((getOthersAccess() & Access.READ) == Access.READ) {
					permissions.add(PosixFilePermission.OTHERS_READ);
				} else {
					permissions.remove(PosixFilePermission.OTHERS_READ);
				}

				if ((getOthersAccess() & Access.WRITE) == Access.WRITE) {
					permissions.add(PosixFilePermission.OTHERS_WRITE);
				} else {
					permissions.remove(PosixFilePermission.OTHERS_WRITE);
				}

				if ((getOthersAccess() & Access.EXECUTE) == Access.EXECUTE) {
					permissions.add(PosixFilePermission.OTHERS_EXECUTE);
				} else {
					permissions.remove(PosixFilePermission.OTHERS_EXECUTE);
				}

				Files.setPosixFilePermissions(Paths.get(file.toURI()),
						permissions);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set the all access rights.
	 * 
	 * Compares the groups with a binary and. If the key were using matches with
	 * the groups bit position then set the value for that group.
	 * 
	 * @param value
	 *            The rights value.
	 */
	public void setAccess(int key, int value) {
		if ((key & Group.OWNER) == Group.OWNER) {
			packAccess(value, getGroupAccess(), getOthersAccess());
		}
		if ((key & Group.GROUP) == Group.GROUP) {
			packAccess(getOwnerAccess(), value, getOthersAccess());
		}
		if ((key & Group.OTHERS) == Group.OTHERS) {
			packAccess(getOwnerAccess(), getGroupAccess(), value);
		}
	}

	/**
	 * Reset all access rights to none.
	 */
	public void resetAccess() {
		access = Access.NONE;
	}

	/**
	 * Pack the access rights.
	 * 
	 * Each value is only 3 bits maxing out at 7 decimal or in binary 111. With
	 * this information we can store each value in three sets of three bits.
	 * 
	 * Others 0-2 Bits, Group 3-5 Bits, Owner 6-8 Bits.
	 * 
	 * To position the values we shift over the values to their starting bit.
	 * 
	 * Others Bit 0 (No shift). Group Bit 3. Owner Bit 6.
	 * 
	 * @param owner
	 *            Owner access rights value.
	 * 
	 * @param group
	 *            Group access rights value.
	 * 
	 * @param others
	 *            Others access rights value.
	 */
	public void packAccess(int owner, int group, int others) {
		access = (owner << 6) | (group << 3) | others;
	}

	/**
	 * The value for the owner access rights.
	 * 
	 * Shift the access rights back to the start 6 bits and get the first 3
	 * bits.
	 * 
	 * @return permissions
	 */
	public int getOwnerAccess() {
		return (access >> 6) & 0x7;
	}

	/**
	 * The value for the group access rights.
	 * 
	 * Shift the access rights to the right 3 bits and get the first 3 bits.
	 * 
	 * @return permissions
	 */
	public int getGroupAccess() {
		return (access >> 3) & 0x7;
	}

	/**
	 * The value for the others access rights.
	 * 
	 * Get the first 3 bits of the access rights.
	 * 
	 * @return permissions
	 */
	public int getOthersAccess() {
		return access & 0x7;
	}

	/**
	 * The permissions in integer form.
	 * 
	 * @return permissions.
	 */
	public int getAccess() {
		return access;
	}

	/**
	 * The file to set permissions for.
	 * 
	 * @return file.
	 */
	public File getFile() {
		return file;
	}

	@Override
	public String toString() {
		return file.getName() + ": " + getOwnerAccess() + "" + getGroupAccess()
				+ "" + getOthersAccess() + " " + Integer.toBinaryString(access);
	}
}
