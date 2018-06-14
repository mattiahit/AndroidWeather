package pl.mattiahit.androidweather.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mattiahit.androidweather.adapters.FavouriteLocationAdapter;
import pl.mattiahit.androidweather.MainActivity;
import pl.mattiahit.androidweather.R;
import pl.mattiahit.androidweather.async.DeleteFavLocation;
import pl.mattiahit.androidweather.async.GetFavLocations;
import pl.mattiahit.androidweather.dialogs.ConfirmDialog;
import pl.mattiahit.androidweather.models.FavouriteLocation;

public class FavouriteListFragment extends Fragment {

    private MainActivity mainActivity;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<FavouriteLocation> favList;

    @BindView(R.id.fav_list)
    RecyclerView fav_list;

    @BindString(R.string.delete_from_favourities_question)
    String delete_from_favourities_question;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_favourities, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        this.mainActivity = (MainActivity) this.getActivity();
        this.initFavList();


        super.onResume();
    }

    private void initFavList(){
        GetFavLocations getFavLocations = new GetFavLocations(this.mainActivity.getAppDatabase());
        getFavLocations.setListener(new GetFavLocations.AsyncTaskListener() {
            @Override
            public void onAsyncTaskFinished(List<FavouriteLocation> value) {
                favList = value;
                fav_list.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(mainActivity);
                fav_list.setLayoutManager(mLayoutManager);

                FavouriteLocationAdapter favouriteLocationAdapter = new FavouriteLocationAdapter(mainActivity, value){
                    public void doOnClick(final int position){
                        final ConfirmDialog confirmDialog = ConfirmDialog.newInstance(delete_from_favourities_question);
                        confirmDialog.setConfirmListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                confirmDialog.dismiss();
                                DeleteFavLocation deleteFavLocation = new DeleteFavLocation(mainActivity.getAppDatabase(), favList.get(position));
                                deleteFavLocation.setListener(new DeleteFavLocation.AsyncTaskListener() {
                                    @Override
                                    public void onAsyncTaskFinished(Integer value) {
                                        initFavList();
                                    }
                                });
                                deleteFavLocation.execute();
                            }
                        });
                        confirmDialog.show(mainActivity.getSupportFragmentManager(),"question_delete_dialog");
                    }
                };
                fav_list.setAdapter(favouriteLocationAdapter);
            }
        });
        getFavLocations.execute();


    }

    @OnClick(R.id.go_home_btn)
    public void goToHome(){
        this.mainActivity.getNavigator().goToHome();
    }
}
