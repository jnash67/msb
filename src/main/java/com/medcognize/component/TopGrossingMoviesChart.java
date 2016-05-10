package com.medcognize.component;

import com.medcognize.MedcognizeUI;
import com.medcognize.domain.Movie;
import org.dussan.vaadin.dcharts.DCharts;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class TopGrossingMoviesChart extends DCharts {

    public TopGrossingMoviesChart() {
        setCaption("Top Grossing Movies");
        // getOptions().setTitle("");
        setSizeFull();

        List<Movie> movies = new ArrayList<Movie>(MedcognizeUI.getDataProvider()
                .getMovies());

    }
}
