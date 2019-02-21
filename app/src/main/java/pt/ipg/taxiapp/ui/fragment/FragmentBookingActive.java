package pt.ipg.taxiapp.ui.fragment;

import android.arch.lifecycle.LiveData;
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
import pt.ipg.taxiapp.data.model.Taxi;
import pt.ipg.taxiapp.ui.main.ActivityBookingActiveDetails;
import pt.ipg.taxiapp.ui.main.BookingViewModel;
import pt.ipg.taxiapp.ui.main.MainActivity;
import pt.ipg.taxiapp.ui.main.TaxiViewModel;
import pt.ipg.taxiapp.utils.Tools;

public class FragmentBookingActive extends Fragment {

    private View root_view;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_booking_active, container, false);
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
        bookingViewModel.getAllActiveBookings().observe(this, new Observer<List<Booking>>() {


            @Override
            public void onChanged(@Nullable List<Booking> bookings) {
                BookingAdapter mAdapter = new BookingAdapter(getActivity(), bookings);
                recyclerView.setAdapter(mAdapter);
             //   List<Booking> bookingList = bookings;

                // on item list clicked
                mAdapter.setOnItemClickListener(new BookingAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, Booking obj, int position) {
                        ActivityBookingActiveDetails.navigate(getActivity(), obj);
                    }
                });


            }
        });


       // List<Booking> bookingList = Tools.getBookingActive(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
