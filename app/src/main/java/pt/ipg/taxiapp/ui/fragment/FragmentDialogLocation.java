package pt.ipg.taxiapp.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.model.LatLng;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import pt.ipg.taxiapp.R;
import pt.ipg.taxiapp.adapter.LocationListAdapter;
import pt.ipg.taxiapp.ui.main.MainActivity;
import pt.ipg.taxiapp.utils.Tools;

public class FragmentDialogLocation extends DialogFragment {

    public CallbackResult callbackResult;
    private String hint = "";

    private GoogleApiClient mGoogleApiClient;
    private LocationListAdapter mAdapter;
    private ItemFilter mFilter = new ItemFilter();
    private ProgressBar progress_bar;

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    private int request_code = 0;
    private View root_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_dialog_location, container, false);
        initComponent();

        Tools.setSystemBarColorFragment(getActivity(), this, R.color.grey_soft);
        Tools.setSystemBarLightFragment(this);
        Tools.checkInternetConnection(getActivity());

        return root_view;
    }

    private void initComponent() {
        final ImageView img_clear = (ImageView) root_view.findViewById(R.id.img_clear);
        final EditText et_search = (EditText) root_view.findViewById(R.id.et_search);
        RecyclerView recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        progress_bar = (ProgressBar) root_view.findViewById(R.id.progress_bar);

        //set data and list adapter
        mAdapter = new LocationListAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        et_search.setHint(hint);

        progress_bar.setVisibility(View.GONE);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = et_search.getText().toString().trim();
                if (!str.equals("")) {
                    img_clear.setVisibility(View.VISIBLE);
                    mFilter.filter(str);
                    progress_bar.setVisibility(View.VISIBLE);
                } else {
                    img_clear.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAdapter.setOnItemClickListener(new LocationListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, AutocompletePrediction obj, int position) {
                // enviar PlaceId no array para marcar ponto
                String[] str = new String[3];
                str[0] = obj.getPrimaryText(null).toString();
                str[1] = obj.getPlaceId();
                sendDataResult(str);
                dismissDialog();
            }
        });

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });

        ((ImageView) root_view.findViewById(R.id.img_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        ((View) root_view.findViewById(R.id.lyt_select_from_map)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "A sua localização no mapa", Toast.LENGTH_SHORT).show();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), 0, onConnectionFailedListener)
                .addApi(Places.GEO_DATA_API)
                .build();

        Tools.hideKeyboardFragment(this);
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<AutocompletePrediction> filterData = new ArrayList<>();
            if (constraint != null) filterData = requestPredictionToGoogle(constraint.toString());
            results.values = filterData;
            if (filterData != null) {
                results.count = filterData.size();
            } else {
                results.count = 0;
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<AutocompletePrediction> items = (ArrayList<AutocompletePrediction>) results.values;
            if (items == null) items = new ArrayList<>();
            mAdapter.setItems(items);
            progress_bar.setVisibility(View.GONE);
        }

    }

    //https://code.tutsplus.com/articles/google-play-services-using-the-places-api--cms-23715
    private ArrayList<AutocompletePrediction> requestPredictionToGoogle(String keyword) {
        if (mGoogleApiClient.isConnected()) {
            //https://stackoverflow.com/questions/34018781/how-to-get-country-specific-results-in-google-places-api-for-autocompletepredict/39785267
           // AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().
           //         setTypeFilter(Place.TYPE_COUNTRY).setCountry("PT").build();

            PendingResult<AutocompletePredictionBuffer> results;
            results = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, keyword.toString(), null, null);

            AutocompletePredictionBuffer autocompletePredictions = results.await(60, TimeUnit.SECONDS);
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                autocompletePredictions.release();
                Toast.makeText(getActivity(), "Error : " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                return null;
            }

            return DataBufferUtils.freezeAndClose(autocompletePredictions);
        }
        return null;
    }

    private OnConnectionFailedListener onConnectionFailedListener = new OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
        }
    };

    private void sendDataResult(String[] loc) {
        if (callbackResult != null) {
            callbackResult.sendResult(request_code, loc);
         }
    }

    private void dismissDialog() {
        Tools.hideKeyboardFragment(this);
        dismiss();
    }

    @Override
    public int getTheme() {
        return R.style.AppTheme_FullScreenDialog;
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setRequestCode(int request_code) {
        this.request_code = request_code;
    }

    public interface CallbackResult {
        void sendResult(int requestCode, String[] loc);
    }

}