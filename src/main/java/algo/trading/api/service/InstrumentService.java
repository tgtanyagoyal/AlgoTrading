package algo.trading.api.service;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;

import java.io.IOException;
import java.text.ParseException;

public interface InstrumentService {

	String processRequest(String instrumentToken, String startDate, String endDate) throws IOException, ParseException, KiteException;
}
