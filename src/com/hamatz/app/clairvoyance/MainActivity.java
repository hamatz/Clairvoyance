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
import android.widget.TextView;

public class MainActivity<clairvoyance> extends Activity {
    /** Called when the activity is first created. */

    private PackageManager mPackageManager;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mPackageManager = getApplicationContext().getPackageManager();
        List<ApplicationInfo> applicationInfoList = mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        final String[] blackList = getResources().getStringArray(R.array.blacklist);
        final TextView textView = (TextView) findViewById(R.id.text);
        
        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Intent it = new Intent();
                it.setAction(Intent.ACTION_SEND);
                it.setType("text/plain");
                it.putExtra(Intent.EXTRA_TEXT, textView.getText().toString());
                startActivity(it);
            }
        });
        
        findViewById(R.id.btn_uninstall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Intent intent = new Intent(MainActivity.this, DeleteOpActivity.class);
                
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartApp();
            }
        });
        
        int total = 0;
        final StringBuilder sb = new StringBuilder();

        for (String target : blackList){
            int subTotal = 0;
            sb.append("指定賞金首名：").append(target).append("\n");
            for (ApplicationInfo applicationInfo : applicationInfoList) {
                String packageName = applicationInfo.packageName;
                try{
                    mPackageManager.getActivityInfo(new ComponentName(packageName, target), 0);
                    sb.append("アプリ名：[").append(mPackageManager.getApplicationLabel(applicationInfo).toString()).append("]\n");
                    sb.append(packageName).append("\nから賞金首を検出しました！\n\n");
                    total++;
                    subTotal++;
                }catch( Exception ce){
                }
            }
            if ( 0==subTotal){
                sb.append("該当するアプリはありませんでした\n\n");
            }
        }
        sb.append("\n"+"賞金首を合計で").append(total).append("件検出しました\n");

        if ( 0==total){
            sb.append("おめでとう！賞金首は見つかりませんでした！\n");
        }
        textView.setText(sb);
    }

    /**
     * アプリを強制終了させ、再起動する
     * @param null
     */
    public void restartApp(){
        finish();
        Intent rstit = new Intent(this, MainActivity.class);
        startActivity(rstit);
    }

}
