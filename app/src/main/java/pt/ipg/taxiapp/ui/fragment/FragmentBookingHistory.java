package pt.ipg.taxiapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import java.util.List;

import pt.ipg.taxiapp.R;
import pt.ipg.taxiapp.adapter.BookingListAdapter;
import pt.ipg.taxiapp.data.model.Booking;
import pt.ipg.taxiapp.ui.main.ActivityBookingHistoryDetails;
import pt.ipg.taxiapp.utils.Tools;

public class FragmentBookingHistory extends Fragment {

    private View root_view;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_booking_history, container, false);
        initComponent();
        return root_view;
    }

    private void initComponent() {
        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        // receber da API ---todo vers 0.7
        List<Booking> bookingList = Tools.getBookingHistory(getActivity());


        BookingListAdapter mAdapter = new BookingListAdapter(getActivity(), bookingList);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new BookingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Booking obj, int position) {
                ActivityBookingHistoryDetails.navigate(getActivity(), obj);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
