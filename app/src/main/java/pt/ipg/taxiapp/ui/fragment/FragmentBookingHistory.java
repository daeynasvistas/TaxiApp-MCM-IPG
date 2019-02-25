package pt.ipg.taxiapp.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import pt.ipg.taxiapp.adapter.BookingAdapter;
import pt.ipg.taxiapp.data.model.Booking;
import pt.ipg.taxiapp.ui.main.ActivityBookingActive;
import pt.ipg.taxiapp.ui.main.ActivityBookingHistory;
import pt.ipg.taxiapp.ui.main.BookingViewModel;
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

    private BookingViewModel bookingViewModel;
    private void initComponent() {
        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        bookingViewModel = ViewModelProviders.of(this).get(BookingViewModel.class);
        // receber do room todos os ACTIVE getallactivebookings()
        bookingViewModel.getAllFinishedBookings().observe(this, new Observer<List<Booking>>() {
            @Override
            public void onChanged(@Nullable List<Booking> bookings) {
                // colocar room bookings database no adapter
                BookingAdapter mAdapter = new BookingAdapter(getActivity(), bookings);
                recyclerView.setAdapter(mAdapter);
                //   List<Booking> bookingList = bookings;

                mAdapter.setOnItemClickListener(new BookingAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, Booking obj, int position) {
                        ActivityBookingHistory.navigate(getActivity(), obj);
                    }
                });


            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
