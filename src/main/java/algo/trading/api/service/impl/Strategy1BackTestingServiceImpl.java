package algo.trading.api.service.impl;

import algo.trading.api.config.KiteConnectConfig;
import algo.trading.api.dto.Candle;
import algo.trading.api.service.Strategy1BackTestingService;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class Strategy1BackTestingServiceImpl implements Strategy1BackTestingService {

    private int quantity = 10;
    private final KiteConnect kiteConnect;

    public Strategy1BackTestingServiceImpl(KiteConnect kiteConnect) {
        this.kiteConnect = kiteConnect;
    }

    @Override
    public void runBacktest(String instrumentToken) throws IOException, KiteException, ParseException {

        // 1️⃣ Fetch historical 5-min candles
        List<Candle> allCandles = fetch5MinCandles(instrumentToken);

        // 2️⃣ Group candles by day
        Map<String, List<Candle>> candlesByDay = groupCandlesByDay(allCandles);

        Map<String, Double> daywisePnL = new LinkedHashMap<>();

        // 3️⃣ Loop through each day
        for (String day : candlesByDay.keySet()) {
            List<Candle> candles = candlesByDay.get(day);
            double dailyPnL = 0;

            for (int i = 10; i < candles.size(); i++) {

                Candle current = candles.get(i);

                double highest10 = candles.subList(i - 10, i).stream().mapToDouble(c -> c.close).max().orElse(0);
                double lowest10 = candles.subList(i - 10, i).stream().mapToDouble(c -> c.close).min().orElse(0);

                // -----------------------------
                // Buy Signal
                // -----------------------------
                if (current.close > highest10) {
                    double entryPrice = current.close;
                    double stopLoss = entryPrice * 0.995; // 0.5% SL
                    double target = entryPrice * 1.01;   // 1% Target

                    double pnl = simulateIntradayTrade(candles.subList(i, candles.size()), entryPrice, stopLoss, target, "BUY");
                    dailyPnL += pnl;
                }

                // -----------------------------
                // Sell Signal
                // -----------------------------
                if (current.close < lowest10) {
                    double entryPrice = current.close;
                    double stopLoss = entryPrice * 1.005; // 0.5% SL
                    double target = entryPrice * 0.99;    // 1% Target

                    double pnl = simulateIntradayTrade(candles.subList(i, candles.size()), entryPrice, stopLoss, target, "SELL");
                    dailyPnL += pnl;
                }
            }

            daywisePnL.put(day, dailyPnL);
        }

        // 4️⃣ Print day-wise PnL
        System.out.println("Date\t\tPnL");
        double totalPnL = 0;
        for (String day : daywisePnL.keySet()) {
            double pnl = daywisePnL.get(day);
            System.out.println(day + "\t" + pnl);
            totalPnL += pnl;
        }
        System.out.println("Total PnL for period: " + totalPnL);
    }

    private double simulateIntradayTrade(List<Candle> remainingCandles, double entryPrice, double stopLoss, double target, String side) {

        for (Candle c : remainingCandles) {
            if (side.equals("BUY")) {
                if (c.low <= stopLoss) return (stopLoss - entryPrice) * quantity; // hit stop-loss
                if (c.high >= target) return (target - entryPrice) * quantity;      // hit target
            } else {
                if (c.high >= stopLoss) return (entryPrice - stopLoss) * quantity; // stop-loss
                if (c.low <= target) return (entryPrice - target) * quantity;      // target
            }
        }
        // if neither hit, exit at last candle close
        Candle last = remainingCandles.get(remainingCandles.size() - 1);
        return (side.equals("BUY") ? (last.close - entryPrice) : (entryPrice - last.close)) * quantity;
    }

    private Map<String, List<Candle>> groupCandlesByDay(List<Candle> candles) {
        Map<String, List<Candle>> map = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Candle c : candles) {
            String day = sdf.format(c.timestamp);
            map.putIfAbsent(day, new ArrayList<>());
            map.get(day).add(c);
        }
        return map;
    }

    private List<Candle> fetch5MinCandles(String instrumentToken) throws IOException, KiteException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"); // updated
        Date from = formatter.parse("2026-01-01T09:15:00+0530");
        Date to = formatter.parse("2026-01-31T15:30:00+0530");

        HistoricalData historicalData = kiteConnect.getHistoricalData(from, to, instrumentToken, "5minute", false, true);

        List<Candle> candleList = new ArrayList<>();
        if (historicalData != null && historicalData.dataArrayList != null) {
            for (HistoricalData data : historicalData.dataArrayList) {
                Date timestamp = formatter.parse(data.timeStamp); // parse ISO8601 timestamp
                Candle candle = new Candle(data.high, data.low, data.close, data.volume, timestamp);
                candleList.add(candle);
            }
        }
        return candleList;
    }

}
