window.com_medcognize_view_dashboard_Highcharts = function () {
    this.onStateChange = function () {
        var chart = new Highcharts.Chart
        (this.getState().data);
    }
}