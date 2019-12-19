import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class LinearRegression {

    private int dataSetSize = 1001; //количество пар в датасете

    private double[] dataSetX = new double[dataSetSize]; //x
    private double[] dataSetY = new double[dataSetSize]; //y (реальное значение)

    private double[] noiseDataSetY = new double[dataSetSize]; //y + noise


    private double f(double x){
        return (Math.pow(1.1, x) - 2);
    }

    private void generateDataSet(){ //генерирование датасета с добавлением шума
        double step = 0.003;
        double currX = 0;
        for (int i = 0; i < dataSetSize; i++){
            dataSetX[i] = currX;
            dataSetY[i] = f(currX);

            //добавление шума
            java.util.Random r = new java.util.Random();
            noiseDataSetY[i] = dataSetY[i] + r.nextGaussian() * 0.02;

            currX += step;
        }
    }

    private void printDataSet(double[] xData, double yData[], int size){
        for (int i = 0; i < size; i++){
            System.out.println((i+1) + ") x = " + xData[i] + "; y = " + yData[i]);
        }
    }

    private void showGraph(){
        // Create Chart
        XYChart chart = new XYChartBuilder().width(600).height(500).title("Test chart").xAxisTitle("X").yAxisTitle("Y").build();

        // Customize Chart
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setChartTitleVisible(false);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);
        chart.getStyler().setMarkerSize(5);

        // Series
        chart.addSeries("Real value", dataSetX, dataSetY);
        XYSeries series = chart.addSeries("Value with noise",dataSetX, noiseDataSetY);
        series.setMarker(SeriesMarkers.DIAMOND);

        new SwingWrapper(chart).displayChart();
    }

    public void solve(){
        generateDataSet();
        System.out.println("Датасет без шума:");
        printDataSet(dataSetX, dataSetY, dataSetSize);
        System.out.println("Датасет с шумом:");
        printDataSet(dataSetX, noiseDataSetY, dataSetSize);
        showGraph();
    }

}
