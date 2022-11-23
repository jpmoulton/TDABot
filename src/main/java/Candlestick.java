import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Candlestick implements java.io.Serializable{
    Double o;
    Double h;
    Double l;
    Double c;
    Double vwap;
    Double volume;
    Integer count;
    String time;

    String interval;
    Integer unixTime;
    boolean isLive = false;
    String symbol;
    String curType;
    boolean isGreen = false;
    boolean isRed = false;

    /**
     * Candlestick constructor, initializes a new Candlestick object
     */
    public Candlestick() {}

    @Override
    public String toString() {
        String color = isGreen ? "Green" : "Red";
        return "Candlestick{" +
                "isLive = " + isLive +
                ", Symbol =  " + symbol +
                ", Time = " + time +
                ", Color = " + color +
                ", Open = " + o +
                ", High = " + h +
                ", Low = " + l +
                ", Close = " + c +
                '}';
    }

    public Double getO() {
        return o;
    }

    public void setO(Double o) {
        this.o = o;
    }

    public Double getH() {
        return h;
    }

    public void setH(Double h) {
        this.h = h;
    }

    public Double getL() {
        return l;
    }

    public void setL(Double l) {
        this.l = l;
    }

    public Double getC() {
        return c;
    }

    public void setC(Double c) {
        this.c = c;
    }

    public String getTime() {
        return time;
    }

    public void setTime(Long time) {
        final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final long unixTime = time;
        this.time = Instant.ofEpochMilli(unixTime)
                .atZone(ZoneId.of("UTC"))
                .format(formatter);
    }

    public Double getVwap() {
        return vwap;
    }

    public void setVwap(Double vwap) {
        this.vwap = vwap;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public boolean isGreen() {
        return isGreen;
    }

    public void setGreen(boolean green) {
        isGreen = green;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean red) {
        isRed = red;
    }

    public String getCurType() {
        return curType;
    }

    public void setCurType(String curType) {
        this.curType = curType;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(Integer unixTime) {
        this.unixTime = unixTime;
    }


    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }


}
