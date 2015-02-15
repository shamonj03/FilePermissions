import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		Main main = new Main();

		main.test();
	}

	private void test() {
		String home = System.getProperty("user.home");
		System.out.println(home);
		System.out.println();

		File desktop_folder = new File(home + "/Desktop");

		if (desktop_folder.exists()) {
			File file = new File(desktop_folder + "/test.sh");

			if (!file.exists()) {
				try {
					BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (file.exists()) {
				// Create new permissions for the file.
				PosixPermissions permissions = new PosixPermissions(file);

				// Set execute access for all groups.
				permissions.setAccess(Group.ALL, Access.ALL);
				System.out.println(permissions);
				
				permissions.setAccess(Group.OWNER | Group.OTHERS, Access.READ);
				System.out.println(permissions);

				permissions.resetAccess();
				permissions.setAccess(Group.OWNER | Group.OTHERS, Access.READ | Access.WRITE);
				System.out.println(permissions);
				
				permissions.setAccess(Group.ALL, Access.NONE);
				System.out.println(permissions);

				permissions.updateFilePermissions();
			}
		} else {
			System.err.println("Could not find desktop.");
		}
	}
}
