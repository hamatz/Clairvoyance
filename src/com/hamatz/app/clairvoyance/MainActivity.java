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
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//import android.widget.Toast;

public class MainActivity<clairvoyance> extends Activity {
    /** Called when the activity is first created. */

    private PackageManager mPackageManager = null;
    private String target = "";
    private String[] blackList = null;
    private int iteration = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mPackageManager = getApplicationContext().getPackageManager();
		List<ApplicationInfo> applicationInfoList = mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA);
		ApplicationInfo applicationInfo;

        blackList = getResources().getStringArray(R.array.blacklist);
        iteration = blackList.length;

	    final TextView textView = (TextView) findViewById(R.id.text);
		
		Button button = (Button) findViewById(R.id.Button01);
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Button button = (Button) v;
                Intent it = new Intent();
                it.setAction(Intent.ACTION_SEND);
                it.setType("text/plain");
                it.putExtra(Intent.EXTRA_TEXT, textView.getText().toString());
                startActivity(it);
            }
        });
        
		Button button2 = (Button) findViewById(R.id.Button02);
        
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Button button2 = (Button) v;
                Intent intent = new Intent(MainActivity.this, DeleteOpActivity.class);
                
                startActivity(intent);
            }
        });

		Button button3 = (Button) findViewById(R.id.Button03);
        
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Button button3 = (Button) v;
                restartApp();
            }
        });
        
		int size = applicationInfoList.size();
		int c=0;
		int c2=0;
		String rs="";

		for (int j = 0; j < iteration; j++){
			target = blackList[j];
			rs+="指定賞金首名：" + target +"\n";
			for (int i = 0; i < size; i++) {
				applicationInfo = applicationInfoList.get(i);
				String packageName = applicationInfo.packageName;
					try{
						mPackageManager.getActivityInfo(new ComponentName(packageName, target), 0);
						rs+="アプリ名：["+mPackageManager.getApplicationLabel(applicationInfo).toString()+"]\n" + packageName +"\nから賞金首を検出しました！\n\n";
						c++;
						c2++;
					}catch( Exception ce){
					}
				}
			if ( 0==c2){
				rs+="該当するアプリはありませんでした\n\n";
			}
			c2 = 0;
		}
			rs=rs+"\n"+"賞金首を合計で"+c+"件検出しました\n";

			if ( 0==c){
				rs+="おめでとう！賞金首は見つかりませんでした！\n";
			}
		    textView.setText(rs);
		}

	/**
	 * アプリを強制終了させ、再起動する
	 * @param null
	 */
	public void restartApp(){
		finish();
		Intent rstit = new Intent(MainActivity.this, MainActivity.class);
		startActivity(rstit);
	}

}
