package com.medcognize.component;

import com.medcognize.MedcognizeUI;
import com.medcognize.domain.Movie;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class TopSixTheatersChart extends DCharts {

    public TopSixTheatersChart() {
        SeriesDefaults seriesDefaults = new SeriesDefaults()
                .setRenderer(SeriesRenderers.PIE);
        Options options = new Options()
                .setSeriesDefaults(seriesDefaults);

        setCaption("Popular Movies");
        getOptions().setTitle("");
        setWidth("100%");
        setHeight("90%");

        DataSeries series = new DataSeries();

        List<Movie> movies = new ArrayList<Movie>(MedcognizeUI.getDataProvider()
                .getMovies());
        for (int i = 0; i < 6; i++) {
            Movie movie = movies.get(i);
            series.add(movie.getTitle(),
                    movie.getScore());
        }
        setDataSeries(series);
    }

}
