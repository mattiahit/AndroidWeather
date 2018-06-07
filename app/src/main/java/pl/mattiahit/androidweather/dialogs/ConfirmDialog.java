package pl.mattiahit.androidweather.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mattiahit.androidweather.R;

public class ConfirmDialog extends DialogFragment {

    public static final String DIALOG_TITLE_TAG = "DIALOG_TITLE";

    @BindView(R.id.confirmQuestion)
    TextView confirmQuestion;
    @BindView(R.id.button_confirm)
    Button button_confirm;
    @BindView(R.id.button_cancel)
    Button button_cancel;

    private View.OnClickListener listener;

    private String title = "";

    public static ConfirmDialog newInstance(String title) {
        ConfirmDialog frag = new ConfirmDialog();
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE_TAG, title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_confirm, container, false);
        ButterKnife.bind(this, v);

        this.title = this.getArguments().getString(DIALOG_TITLE_TAG);
        this.button_confirm.setOnClickListener(this.listener);
        confirmQuestion.setText(this.title);

        return v;
    }

    public void setConfirmListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @OnClick(R.id.button_cancel)
    public void doOnCancel(){
        this.dismiss();
    }
}