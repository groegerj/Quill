package name.vbraun.filepicker;

import java.io.File;

public class Shortcut {
	
	enum Type { ORDINARY_DIR, UP_DIRECTORY, SEARCH };
	
	protected Shortcut(String title, int icon, Type type, String filename) {
		this.title = title;
		this.icon = icon;
		this.type = type;
		if (type.equals(Type.ORDINARY_DIR)) {
			file = new File(filename);
			enabled = file.exists();
		} else {
			file = null;
			enabled = false;
		}
	}            

	
	protected String title;
	protected int icon;
	protected File file;
	protected Type type;
	protected boolean enabled;
	
}
