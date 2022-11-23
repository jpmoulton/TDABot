import java.util.ArrayList;
import java.util.Arrays;

public class OrderBlock implements java.io.Serializable {

    Candlestick candle;
    //Orders associated with this specific order block
//    ArrayList<Order> orderList;
    String orderBlockID;
    boolean ordersPlaced;
    boolean priceHasHit;
    int interval;
    double stopLoss;
    //List of prices for buying order blocks. Index 0 corresponds to the 0 price, AKA the high of the bear candle.
    public Double[] entryPoints;
    Double percentFromMarket;

    public OrderBlock(Candlestick candle, int interval) {
        this.candle = candle;
//        this.orderList = new ArrayList<>();
        this.interval = interval;

        entryPoints = new Double[5];
        double priceMovement = candle.getH() - candle.getL();
        double zero = (candle.getH());
        double quarter = candle.getH() - (priceMovement * .25);
        double half = candle.getH() - (priceMovement * .5);
        double threeQuarters = candle.getH() - (priceMovement * .75);
        double whole = candle.getL();

        entryPoints[0] = zero;
        entryPoints[1] = quarter;
        entryPoints[2] = half;
        entryPoints[3] = threeQuarters;
        entryPoints[4] = whole;
        if(interval < 480) {
            stopLoss = candle.getH() - (priceMovement * .51);
        } else {
            stopLoss = candle.getL() - (priceMovement * 0.015);
        }

    }


    public double getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(double stopLoss) {
        this.stopLoss = stopLoss;
    }

    public Double getPercentFromMarket() {
        return percentFromMarket;
    }

    public void setPercentFromMarket(Double percentFromMarket) {
        this.percentFromMarket = percentFromMarket;
    }

    public Candlestick getCandle() {
        return candle;
    }

    public void setCandle(Candlestick candle) {
        this.candle = candle;
    }

//    public ArrayList<Order> getOrderList() {
//        return orderList;
//    }
//
//    public void addOrder(Order order) {
//        this.orderList.add(order);
//    }
//
//    public void setOrderList(ArrayList<Order> orderList) {
//        this.orderList = orderList;
//    }

    public boolean isOrdersPlaced() {
        return ordersPlaced;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }


    public void setOrdersPlaced(boolean ordersPlaced) {
        this.ordersPlaced = ordersPlaced;
    }

    public boolean isPriceHasHit() {
        return priceHasHit;
    }

    public void setPriceHasHit(boolean priceHasHit) {
        this.priceHasHit = priceHasHit;
    }


    public String getOrderBlockID() {
        return orderBlockID;
    }

    public void setOrderBlockID(String orderBlockID) {
        this.orderBlockID = orderBlockID;
    }


    @Override
    public String toString() {
        return "OrderBlock{" +
                "ticker =" + candle.symbol +
                ", time=" + candle.getTime() +
                ", priceHasHit=" + priceHasHit +
                ", percentFromMarket=" + percentFromMarket +
                ", interval=" + interval +
                ", stopLoss=" + stopLoss +
                ", entryPoints= " + Arrays.toString(entryPoints) +
                '}';
    }


}
