import com.studerw.tda.client.HttpTdaClient;
import com.studerw.tda.client.TdaClient;
import com.studerw.tda.model.history.*;
import com.studerw.tda.model.quote.EquityQuote;
import com.studerw.tda.model.quote.Quote;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {
    private static final String CSV_SEPARATOR = ",";

    static ArrayList<OrderBlock> OBList;

    public static void main(String args[]) {

        OBList = new ArrayList<>();

        Properties props = new Properties();
        props.setProperty("tda.client_id", "GKHGAQOM0CT8O2I1GKGVIL6MXLQRR81T");
        props.setProperty("tda.token.refresh", "uudnTTWZ8hvmU6MlrnYw3Gwop1WgUStlWsqmdMj+XdIIA0FNrecxqkLYbf5HGagGP0vEIxw89L0DuKgmJBs7LWH+23oSGuvEYEezTxw/mC3PwgQrQFESuB1wz7mgMqtU4cqNPq+57OAkBkqDopJLWpO7ljJE9PCKpj923vuXUgVfyuRw0zDgn4JM9HQjjTtqGrsdFKy8NzTw6P3gM81HIHJ0PXRSO3HUdXBTm8OytWNkktTYjRCXuYMSL3CxVOYt9Cs5L55fhVlZ78IR8nqDxYpEV+R3ljasABVJOKu5oZkK9Nqo2rIjp7XUOcJTD72IfAZxwl8omqWLbAKFvd+dR0W315Er2YBduhQh14SGFcCXkgQvuI5WGNp/LjAf5xMFOGd2U0kGcIfPhhMXHzVD8JWL92KhP25l4VMPPah7tyv1dv4wKi3wv9Q8JOY100MQuG4LYrgoVi/JHHvln3/KZNbzCikdC2Cx5h4FlgraZFvz69ISYampVU9TYt5PpweXXNvIjSo2pZRHgq6JRobxJPwG9rdXY4j8+yUzMS7+mtzSGPE6HOcGel3pHV05j2aq6P1+WuNFLYb3ewdo9MTBwy6hvGnqUbOIhdg7qPbduzml3j6rXJtb2yDe8E5BvoyUD+mBPIEza2ivSXCc7FrwiVUZA47WLxWkE9G7WA7PcrNkyVff8qTnYyN79BsmYuY1IV18xc4Yr1hpglif62btFih2r1+85rHkOJXLCJt67gn9iVHmmaO9BAshnVIlwWD+qyrpL48dVqEQ0P/Il+Vl9t5BV/cc5DH1Wh3ctqb8mIEOcdLCPyPF08KZ81/Zm+fP0+gGtVnFNDAbGVyrN4KprRfhrThtIaMD3NsqRqaDzo/iRZVmDUFudKADdZCYM/cWcx1KKP7QA20=212FD3x19z9sWBHDJACbC00B75E");

        TdaClient tdaClient = new HttpTdaClient(props);

        PriceHistReq request = PriceHistReq.Builder.priceHistReq()
                .withSymbol("SQQQ")
                .withFrequencyType(FrequencyType.minute)
                .withFrequency(30)
                .withExtendedHours(false)
                .build();

        PriceHistory priceHistory = tdaClient.priceHistory(request);

        ArrayList<Candlestick> customCandleList = new ArrayList<>();

        List<Candle> candleList = priceHistory.getCandles();

        System.out.println(priceHistory.getOtherFields());

        for(Candle candle : candleList) {

                Candlestick newCan = new Candlestick();

                newCan.setC(candle.getClose().doubleValue());
                newCan.setO(candle.getOpen().doubleValue());
                newCan.setH(candle.getHigh().doubleValue());
                newCan.setL(candle.getLow().doubleValue());
                newCan.setSymbol(request.getSymbol());


                newCan.setTime(candle.getDatetime());



                if(newCan.getC().compareTo(newCan.getO()) < 0) {
                    newCan.setRed(true);
                } else {
                    newCan.setGreen(true);
                }
                customCandleList.add(newCan);
            }
        ArrayList<OrderBlock> temp = determineOB(customCandleList, 30);



        for(OrderBlock OB : temp) {
            System.out.println(OB);
        }

    }

    /**
     * Returns the first orderblock found within a particular set of candlesticks.
     *
     * @param candleList - List of candles returned from the API call
     * @return A candlestick which meets all the requirements of being an engulfed orderblock
     */

    static ArrayList<OrderBlock> determineOB(ArrayList<Candlestick> candleList, int interval) {

        ArrayList<OrderBlock> obList = new ArrayList<>();

        for (int i = 0; i < candleList.size(); i++) {
            Candlestick OB = candleList.get(i);
            //Logic to find engulfing for red candle
            if (OB.isRed()) {
                if (i + 2 < candleList.size()) {
                    Candlestick nextCandle1 = candleList.get(i + 1);
                    Candlestick nextCandle2 = candleList.get(i + 2);
                    if (nextCandle1.isGreen() && nextCandle2.isGreen()) {
                        //Determine if engulfing candle
                        //if the percent change of the nextCandle1 is > 1%, then the next candle 2 low can be within the bounds of the OB
                        double percentChange = Math.abs((nextCandle1.getL() - nextCandle1.getH()) / nextCandle1.getL() * 100);
                        if (findPercentDifference(OB.getC(), nextCandle1.getO()).compareTo(0.1) <= 0 &&
                                findPercentDifference(OB.getH(), nextCandle1.getC()).compareTo(0.1) >= 0 ||
                                (OB.getC().compareTo(nextCandle1.getO()) > 0 && OB.getH().compareTo(nextCandle1.getC()) < 0)
                        ) {
                            //If the second candle when validating the OB is live, we don't want to validate the OB yet since it's still in progress and could end red
                            if (!nextCandle2.isLive()) {
                                if (percentChange >= 1.0) {
                                    createOB(candleList, interval, obList, i, OB);
                                } else {
                                    if (!(nextCandle2.getL().compareTo(OB.getH()) < 0)) {
                                        createOB(candleList, interval, obList, i, OB);
                                    }
                                }
                            }

                        }

                    }
                }
            }
        }
        return obList;
    }


    /**
     * Returns the first orderblock found within a particular set of candlesticks.
     *
     * @param candleList - List of candles returned from the API call
     * @return A candlestick which meets all the requirements of being an engulfed orderblock
     */

    private static void createOB(ArrayList<Candlestick> candleList, int interval, ArrayList<OrderBlock> obList, int i, Candlestick OB) {
        OrderBlock newOB = new OrderBlock(OB, interval);
        for (int j = i; j < candleList.size(); j++) {
            //Make sure the candles we are checking are not associated with the OB, AKA they are not the two candles after the OB
            if (i + 2 < j) {
                Candlestick curCandle = candleList.get(j);
                if (curCandle.getL() <= OB.getH()) {
                    newOB.setPriceHasHit(true);
                    break;
                }
            }
        }
        obList.add(newOB);
    }


    /**
     * Utility function to calculate the percent difference between two doubles. Used to find the percent
     * difference between close price of candle a and the open price of candle b.
     *
     * @param a - Close price of first candle
     * @param b - Open price of second candle.
     * @return result - percent difference of two numbers. 0.1 = 0.1%.
     */

    static Double findPercentDifference(Double a, Double b) {
        double result;
        result = ((b - a) * 100) / a;

        return result;
    }

    /**
     * Utility function to export list of order blocks to CSV format for use in spreadsheets.
     *
     * @param OBList   - List of candlesticks that have been identified as order blocks
     * @param interval - Interval of candles (ex. 15 = 15 min candles)
     * @param pair     - Pair of cryptos that are being analyzed to find order blocks on
     */
    static void writeToCSV(ArrayList<Candlestick> OBList, String interval, String pair) throws IOException {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("OB_List_" + pair + "_" + interval + ".csv"), StandardCharsets.UTF_8));
            for (Candlestick candle : OBList) {

                String oneLine = interval +
                        CSV_SEPARATOR +
                        candle.getTime() +
                        CSV_SEPARATOR +
                        candle.getO() +
                        CSV_SEPARATOR +
                        candle.getH() +
                        CSV_SEPARATOR +
                        candle.getL() +
                        CSV_SEPARATOR +
                        candle.getC() +
                        CSV_SEPARATOR +
                        candle.getVolume();
                bw.write(oneLine);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new IOException("Something is wrong with CSV");
        }
    }


}
