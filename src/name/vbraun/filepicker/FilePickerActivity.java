package name.vbraun.filepicker;
import com.write.Quill.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import name.vbraun.filepicker.Shortcut.Type;

public class FilePickerActivity extends Activity implements OnItemClickListener {
	private final static String TAG = "FilePickerActivity";

	/**
     * Activity Action: Pick a file through the file manager, or let user
     * specify a custom file name.
     * Data is the current file name or file name suggestion.
     * Returns a new file name as file URI in data.
     * 
     * <p>Constant filepicker_Value: "org.openintents.action.PICK_FILE"</p>
     */
    public static final String ACTION_PICK_FILE = "org.openintents.action.PICK_FILE";

    /**
     * Activity Action: Pick a directory through the file manager, or let user
     * specify a custom file name.
     * Data is the current directory name or directory name suggestion.
     * Returns a new directory name as file URI in data.
     * 
     * <p>Constant Value: "org.openintents.action.PICK_DIRECTORY"</p>
     */
    public static final String ACTION_PICK_DIRECTORY = "org.openintents.action.PICK_DIRECTORY";
    		
    /**
     * The title to display.
     * 
     * <p>This is shown in the title bar of the file manager.</p>
     * 
     * <p>Constant Value: "org.openintents.extra.TITLE"</p>
     */
    public static final String EXTRA_TITLE = "org.openintents.extra.TITLE";

	
	protected static File currentDir;
	
	private Menu menu;
	private SearchView search;
	
	private ListView shortcutList;
	private ShortcutAdapter shortcutAdapter;
	
	private ScrollView dirBreadcrumbScroll;
	
	private DirList fileList;
	private GridView fileGrid;
	private DirListAdapter fileAdapter;
	
	private TextView dirNameDisplay;
	
	private boolean canSelectDirectory = true;
	private boolean canSelectFile = true;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filepicker_main);
        
        if (currentDir == null)
        	currentDir = new File("/mnt/sdcard");
        
        search = (SearchView) findViewById(R.id.filepicker_search);
        shortcutList = (ListView) findViewById(R.id.filepicker_dir_shortcut_list);
        shortcutAdapter = new ShortcutAdapter(getApplicationContext());
        shortcutList.setAdapter(shortcutAdapter);
        shortcutList.setOnItemClickListener(this);
        
        dirBreadcrumbScroll = (ScrollView) findViewById(R.id.filepicker_dir_breadcrumbs);
        
        dirNameDisplay = (TextView) findViewById(R.id.filepicker_dir_name_display);
        
        fileList = new DirList();
        fileGrid = (GridView) findViewById(R.id.filepicker_file_grid);
        fileAdapter = new DirListAdapter(getApplicationContext(), fileList);
        fileGrid.setOnItemClickListener(this);
        fileGrid.setAdapter(fileAdapter);
        
        Intent intent = getIntent();
        String action = intent.getAction();
        if (action.equals(ACTION_PICK_FILE)) {
        	canSelectFile = true;
        	canSelectDirectory = false;
        	// mFilterFiletype = intent.getStringExtra("FILE_EXTENSION");
            // mFilterMimetype = intent.getType();
        } else if (action.equals(ACTION_PICK_DIRECTORY)) {
        	canSelectFile = false;
        	canSelectDirectory = true;                                                      
        	// mWritableOnly = intent.getBooleanExtra(FileManagerIntents.EXTRA_WRITEABLE_ONLY, false);
        }
        
        String title = intent.getStringExtra(EXTRA_TITLE);
        Log.e(TAG, "title="+title);
        if (title != null) setTitle(title);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
		if (adapter.equals(shortcutList)) {
			Shortcut shortcut = shortcutAdapter.getItem(pos);
			switch (shortcut.type) {
			case ORDINARY_DIR:
				openDirectory(shortcut.file);
				break;
			case UP_DIRECTORY:
				File parent = currentDir.getParentFile();
				if (parent != null)
					openDirectory(parent);
				break;
			}
		} else if (adapter.equals(fileGrid)) {
			DirListEntry entry = fileAdapter.getItem(pos);
			File f = entry.file;
			if (f.isDirectory())
				openDirectory(f);
			else
				pickFile(f);
		}
	}
    
	
	public void newFolder() {
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.filepicker_new_folder_dialog, null);
		
		final EditText text = (EditText)view.findViewById(R.id.filepicker_new_folder_text);

		new AlertDialog.Builder(this)
		.setPositiveButton(R.string.filepicker_new_folder_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String name = text.getText().toString();				
				if(name.length() == 0) return;
				File folder = new File(currentDir, name);
				Log.d(TAG, "Creating new folder "+folder.getAbsolutePath());
				if (folder.mkdir()) {
					openDirectory(folder);
					Toast.makeText(FilePickerActivity.this, "Created folder "+name, Toast.LENGTH_LONG).show();
				} else
					Toast.makeText(FilePickerActivity.this, "Unable to create folder "+name, Toast.LENGTH_LONG).show();
				dialog.dismiss();
			}
		})
		.setNegativeButton(R.string.filepicker_new_folder_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.setView(view)
		.setTitle("Create new folder")
		.setIcon(R.drawable.filepicker_grid_folder).create().show();
	}

	
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	this.menu = menu;
        getMenuInflater().inflate(R.menu.filepicker_menu, menu);
        if (!canSelectDirectory) {
        	MenuItem select = menu.findItem(R.id.filepicker_select_folder);
        	select.setVisible(false);
        }
    	return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		} else if (item.getItemId() == R.id.filepicker_select_folder) {
			pickFile(currentDir);
			return true;
		} else if (item.getItemId() == R.id.filepicker_new_folder) {
			newFolder();
			return true;
		}
    	return super.onOptionsItemSelected(item);
    }
    
    
    
    @Override
    protected void onPause() {
    	super.onPause();
    }

    
    private void pickFile(File file) {
    	if (file.isDirectory() && !canSelectDirectory) {
        	Toast.makeText(getApplicationContext(), "Cannot select a directory.", Toast.LENGTH_LONG).show();
        	return;
    	}
    	if (file.isFile() && !canSelectFile) {
        	Toast.makeText(getApplicationContext(), "Cannot select an ordinary file.", Toast.LENGTH_LONG).show();
        	return;
    	}
        Intent intent = getIntent();
        intent.setData(Uri.fromFile(file));
        setResult(RESULT_OK, intent);
        finish();
        // Toast.makeText(getApplicationContext(), "Picked "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
    }
    
    private void openDirectory(File dir) {
    	currentDir = dir;
		shortcutAdapter.setIndictor(dir);
		fileList.openDirectory(dir);
		fileAdapter.notifyDataSetChanged();
		dirNameDisplay.setText(dir.getAbsolutePath());
    }

    @Override
    protected void onResume() {
    	openDirectory(currentDir);
    	super.onResume();
    }
}