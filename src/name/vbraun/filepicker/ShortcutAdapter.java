package name.vbraun.filepicker;
import com.write.Quill.R;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import name.vbraun.filepicker.Shortcut.Type;

public class ShortcutAdapter extends ArrayAdapter<Shortcut> {

	protected static ArrayList<Shortcut> shortcuts = new ArrayList<Shortcut>();
	private Context context;
	
	public ShortcutAdapter(Context context) {
		super(context, R.layout.filepicker_shortcut_item, shortcuts);
		this.context = context;
		shortcuts.clear();
		addShortcut("Up one directory", R.drawable.filepicker_shortcut_folder, Type.UP_DIRECTORY, null);
		addShortcut("Filesystem",       R.drawable.filepicker_shortcut_folder, Type.ORDINARY_DIR, "/");
		addShortcut("SD card",          R.drawable.filepicker_shortcut_sdcard, Type.ORDINARY_DIR, "/mnt/sdcard");
		addShortcut("External SD card", R.drawable.filepicker_shortcut_sdcard, Type.ORDINARY_DIR, "/mnt/external_sd");
		addShortcut("USB Stick",        R.drawable.filepicker_shortcut_sdcard, Type.ORDINARY_DIR, "/mnt/usbdrive");
		addShortcut("Downloads",        R.drawable.filepicker_shortcut_folder, Type.ORDINARY_DIR, "/mnt/sdcard/Download");
		addShortcut("Music",            R.drawable.filepicker_shortcut_folder, Type.ORDINARY_DIR, "/mnt/sdcard/Music");
		addShortcut("Movies",           R.drawable.filepicker_shortcut_folder, Type.ORDINARY_DIR, "/mnt/sdcard/Movies");
		addShortcut("Pictures",         R.drawable.filepicker_shortcut_folder, Type.ORDINARY_DIR, "/mnt/sdcard/Pictures");
	}
	
	private void addShortcut(String title, int icon, Type type, String dirname) {
		Shortcut s = new Shortcut(title, icon, type, dirname);
		if (s.type.equals(Type.ORDINARY_DIR) && !s.file.exists()) return;
		shortcuts.add(s);
	}
	
	private int indicator_pos = -1;
	
	protected void setIndictor(File file) {
		indicator_pos = -1;
		for (int i=0; i<shortcuts.size(); i++) {
			File f = shortcuts.get(i).file;
			if (f == null) continue;
			if (f.equals(file)) {
				indicator_pos = i;
				break;
			}
		}
		notifyDataSetChanged();
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout;
        if (convertView == null) {
            layout = LayoutInflater.from(context).inflate(R.layout.filepicker_shortcut_item, parent, false);
        } else {
            layout = convertView;
        }
        TextView title = (TextView) layout.findViewById(R.id.filepicker_shortcut_title);
        ImageView icon = (ImageView) layout.findViewById(R.id.filepicker_shortcut_icon);
        ImageView indicator = (ImageView) layout.findViewById(R.id.filepicker_shortcut_indicator);

        Shortcut shortcut = getItem(position);
        title.setText(shortcut.title);
        icon.setImageResource(shortcut.icon);
        title.setEnabled(shortcut.enabled);
        icon.setEnabled(shortcut.enabled);
        
        if (indicator_pos == position)
        	indicator.setVisibility(View.VISIBLE);
        else
        	indicator.setVisibility(View.GONE);
        	
        return layout;
    }

	
	
}
