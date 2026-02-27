package algo.trading.api.service;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;

public interface TokenService {
	String processToken(String requestToken) throws KiteException;
}
