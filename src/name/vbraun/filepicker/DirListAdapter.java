package name.vbraun.filepicker;
import com.write.Quill.R;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DirListAdapter extends ArrayAdapter<DirListEntry> {
	private final static String TAG = "DirListAdapter";

	private Context context;
		
	public DirListAdapter(Context context, DirList listing) {
		super(context, R.layout.filepicker_dir_list_item, listing);
		this.context = context;
	}
	
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout;
        if (convertView == null) {
            layout = LayoutInflater.from(context).inflate(R.layout.filepicker_dir_list_item, parent, false);
        } else {
            layout = convertView;
        }
        TextView title = (TextView) layout.findViewById(R.id.filepicker_grid_title);
        ImageView icon = (ImageView) layout.findViewById(R.id.filepicker_grid_icon);

        DirListEntry entry = getItem(position);
        title.setText(entry.title);
        icon.setImageResource(entry.icon);
        return layout;
    }

	

}
