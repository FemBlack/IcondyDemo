package ind.demo.icondydemo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class IcondyDemoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_icondy_demo);
		Button selectIconPacksButton = (Button) findViewById(R.id.iconpack_list);
		selectIconPacksButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getIconpackList();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			// check if the request code is same as what is passed here it is 2
			if (requestCode == IcondyUtil.REQUEST_CODE_FOR_ICONPACK_LIST) {
				if (resultCode == RESULT_OK) {
					// keys will be the icon pack names and values will be the
					// filepath for the respective icon pack
					// (/storage/emulated/0/ICondy/Ray.zip)
					final Map<String, String> resultMap = (Map<String, String>) data
							.getExtras()
							.get(IcondyUtil.EXTRA_FOR_ICONPACK_LIST);

					if (resultMap != null && resultMap.size() > 0) {
						final String[] stringArray = resultMap.keySet()
								.toArray(new String[resultMap.keySet().size()]);
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								this);
						alertDialog.setItems(stringArray,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										processIconpackFrmGivenFilePath(resultMap
												.get(stringArray[which]));
									}
								});
						alertDialog.create();
						alertDialog.show();
					}

				}
				if (resultCode == RESULT_CANCELED) {
					// Write your code if there's no result
				}
			} else if (requestCode == IcondyUtil.REQUEST_CODE_FOR_ICONPACK) {
				if (resultCode == RESULT_OK) {
					ArrayList<Drawable> drawableList = new ArrayList<Drawable>();
					// keys will be the component name (com.google.android.calendar/com.android.calendar.AllInOneActivity) 
					// values will be the the image file path
					Map<String, String> resultMap = (Map<String, String>) data
							.getExtras().get(IcondyUtil.EXTRA_FOR_ICONPACK);

					if (resultMap != null && resultMap.size() > 0) {
						DisplayIconPack task = new DisplayIconPack();
				        task.execute(new Map[] { resultMap });
					}

				}
				if (resultCode == RESULT_CANCELED) {
					// Write your code if there's no result
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
     * use this method to get the created icon pack list
     *
     */
	private void getIconpackList() {
		Intent iiconpackListIntent = new Intent();
		iiconpackListIntent.setAction(IcondyUtil.ACTION_ICONPACK_LIST);
		iiconpackListIntent.setClassName(IcondyUtil.ICONDY_PACKAGE,
				IcondyUtil.ICONDY_ACTIVITY);
		startActivityForResult(iiconpackListIntent,
				IcondyUtil.REQUEST_CODE_FOR_ICONPACK_LIST);
	}

	/**
     * use this method to process the given file path icon pack and display the same.
     *	filepath for the selected icon pack (/storage/emulated/0/ICondy/Ray.zip)
     */
	private void processIconpackFrmGivenFilePath(String filePath) {
		Intent processIconpackIntent = new Intent();
		processIconpackIntent.setAction(IcondyUtil.ACTION_ICONPACK);
		processIconpackIntent.putExtra(IcondyUtil.EXTRA_FOR_FILEPATH, filePath);
		processIconpackIntent.setClassName(IcondyUtil.ICONDY_PACKAGE,
				IcondyUtil.ICONDY_ACTIVITY);
		startActivityForResult(processIconpackIntent,
				IcondyUtil.REQUEST_CODE_FOR_ICONPACK);
	}
	
	/**
     * use this method to process the given file path icon pack and display the same.
     *	filepath for the selected icon pack (/storage/emulated/0/ICondy/Ray.zip)
     */
	private class DisplayIconPack extends AsyncTask<Map<String, String>, Void, Void> {
		List<Drawable> drawableList;
		AlertDialog.Builder builder;

		@Override
		protected void onPreExecute() {
			drawableList = new ArrayList<Drawable>();
			builder = new AlertDialog.Builder(
					IcondyDemoActivity.this);
		};

		@Override
		protected Void doInBackground(Map<String, String>... params) {
			for (Map<String, String> resultMap : params) {
					
					// use the below code to read all the icons from the zip file
					Collection<String> imageURl = resultMap.values();
					Iterator<String> itr = imageURl.iterator();
					while (itr.hasNext()) {
						String imageUrl = itr.next();
						drawableList.add(IcondyUtil.getImageBitmap(
								imageUrl, getResources()));
					}

					List<String> componentNameList = new ArrayList<String>(
							resultMap.keySet());
					List<ResolveInfo> calendars = IcondyUtil
							.getCalendars(getPackageManager());
					for (ResolveInfo calendar : calendars) {
						// use the below code to read calendar icons based on the component name
						List<String> calenderIconList = IcondyUtil
								.filterCalenderIcons(
										componentNameList,
										new ComponentName(
												calendar.activityInfo.packageName,
												calendar.activityInfo.name)
												.flattenToString());
						Iterator<String> calenderItr = calenderIconList
								.iterator();
						while (calenderItr.hasNext()) {
							String key = calenderItr.next();
							String imageUrl = resultMap.get(key);
							drawableList.add(IcondyUtil.getImageBitmap(
									imageUrl, getResources()));
						}
					}

					final ImageAdapter myAdapter = new ImageAdapter(IcondyDemoActivity.this,
							0, drawableList);
					myAdapter.setNotifyOnChange(true);

					builder.setSingleChoiceItems(myAdapter, 0,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									
								}
							});
					
				}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			builder.create().show();
		};

	}

}
