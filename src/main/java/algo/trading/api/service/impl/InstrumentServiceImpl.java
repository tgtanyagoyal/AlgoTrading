package algo.trading.api.service.impl;

import algo.trading.api.service.InstrumentService;
import algo.trading.api.service.Strategy1BackTestingService;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;

@Service
public class InstrumentServiceImpl implements InstrumentService {

	@Autowired
	private Strategy1BackTestingService strategy1BackTestingService;

	@Override
	public String processRequest(String instrumentToken, String startDate, String endDate) throws IOException, ParseException, KiteException {
		System.out.println("Received instrumentToken=" + instrumentToken + ", startDate=" + startDate + ", endDate=" + endDate);
		strategy1BackTestingService.runBacktest(instrumentToken);
		return null;
	}
}
