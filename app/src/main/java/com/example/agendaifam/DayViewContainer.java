package com.example.agendaifam;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kizitonwose.calendarview.ui.ViewContainer;

public class DayViewContainer extends ViewContainer {
    public TextView dayText;

    public DayViewContainer(@NonNull View view) {
        super(view);
        dayText = view.findViewById(R.id.dayText);
    }
}