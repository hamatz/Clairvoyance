/* Copyright 2011 by Fukuyuki MURAKAMI, Keiji ARIYAMA and Makoto HAMATSU

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.hamatz.app.clairvoyance;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
//import android.widget.Toast;

public class DeleteOpActivity extends Activity{

    private PackageManager mPackageManager = null;
    private int mCount = 0;
    private int mIndex = 0;
    private String[] blackList = null;
    private int iteration = 0;
    private String target = "";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete);
        mPackageManager = getApplicationContext().getPackageManager();
        
        blackList = getResources().getStringArray(R.array.blacklist);
        iteration = blackList.length;
 
       	checkList(0);

    }
    
    private void checkList(int i){
        
        mIndex = 0;
		target = blackList[i];
		
		if (!searchWanted()) {
			if (i < iteration -1){
				i++;
				checkList(i);
			}else {
				alertFinish();
			}
		}
    }

    private boolean searchWanted() {
        List<ApplicationInfo> applicationInfoList = mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        int size = applicationInfoList.size();
        while ( mIndex < size ) {
            ApplicationInfo applicationInfo = applicationInfoList.get(mIndex);
            mIndex++;
            String packageName = applicationInfo.packageName;
                try{
                    mPackageManager.getActivityInfo(new ComponentName(packageName, target), 0);
                    String title = mPackageManager.getApplicationLabel(applicationInfo).toString();
                    deleteApplicationDialog(title, packageName);
                    mCount++;
                    return true;
                } catch ( NameNotFoundException e ) {
                }
        }
        return false;
    }
    
    private void deleteApplication( String packageName ) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        startActivityForResult(intent, 0x01);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x01) {
            if ( !searchWanted() ) {
                alertFinish();
            }
        }
    }
    
    private void deleteApplicationDialog( String title, final String packageName ) {
        new AlertDialog.Builder(this)
        .setTitle( "賞金首を捕まえました！" )
        .setMessage( String.format("アプリケーション %s を、削除しますか？", title) )
        .setPositiveButton( "削除" , new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteApplication( packageName );
            }
        })
        .setNegativeButton( "見逃す" , new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if ( !searchWanted() ) {
                    alertFinish();
                }
            }
        })
        .show();
    }
    
    void alertFinish(){
        String msg = mCount == 0 ? "削除する賞金首はいないようです！"
                : String.format("合計%d件の賞金首を退治しました", mCount);
    	new AlertDialog.Builder(this)
        .setTitle(R.string.app_name)
        .setMessage( msg )
        .setPositiveButton( "OK" , new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface dialog, int which) {
        		finish();
        	}
        })
        .show();
    }
}

