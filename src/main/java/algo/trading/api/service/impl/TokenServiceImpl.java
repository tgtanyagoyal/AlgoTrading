package algo.trading.api.service.impl;

import algo.trading.api.service.KiteConnectService;
import algo.trading.api.service.TokenService;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private KiteConnectService kiteConnectService;

	@Override
	public String processToken(String requestToken) throws KiteException {

		kiteConnectService.kiteConnect(requestToken);

		return "Kite Connection Successful";
	}
}
