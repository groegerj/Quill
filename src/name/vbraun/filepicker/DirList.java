package name.vbraun.filepicker;

import java.io.File;
import java.util.ArrayList;


public class DirList extends ArrayList<DirListEntry> {
	private final static String TAG = "DirList";
	
	protected void openDirectory(File dir) {
		clear();
		File listing[] = dir.listFiles();
		if (listing == null) return;
		for (File entry : listing)  {
			add(new DirListEntry(entry));
		}
	}
	

	
}
