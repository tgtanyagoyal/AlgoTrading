package algo.trading.api.service;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;

import java.io.IOException;
import java.text.ParseException;

public interface Strategy1BackTestingService {

    void runBacktest(String instrumentToken) throws IOException, KiteException, ParseException;
}
