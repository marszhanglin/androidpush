package net.evecom.androidecssp;

import android.os.Bundle;
import android.view.View;

import net.evecom.androidecssp.base.BaseActivity;
import net.evecom.androidecssp.base.BaseWebActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @AfterViews
    public void AfterViews(){
        toast(this.getLocalClassName(),1);
    }


    @Click(R.id.main_btn)
    public void tz(View view){
        BaseWebActivity_.intent(instance).extra("requestUrl","spring/test/param/111").start();
    }


}
