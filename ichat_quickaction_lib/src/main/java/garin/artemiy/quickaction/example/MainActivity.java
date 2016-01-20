package garin.artemiy.quickaction.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import garin.artemiy.quickaction.R;
import garin.artemiy.quickaction.library.QuickAction;

public class MainActivity extends Activity {

    private QuickAction quickAction;

    @SuppressWarnings("InflateParams")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        RelativeLayout customLayout = (RelativeLayout)
                getLayoutInflater().inflate(R.layout.popup_custom_layout, null);

        quickAction = new QuickAction
                (this, R.style.PopupAnimation, customLayout, customLayout);
    }

    @SuppressWarnings("unused")
    public void onClickTopButton(View view) {
        quickAction.show(view);
    }

    @SuppressWarnings("unused")
    public void onClickMiddleButton(View view) {
        quickAction.show(view);
    }

    @SuppressWarnings("unused")
    public void onClickBottomButton(View view) {
        quickAction.show(view);
    }

}
