package algo.trading.api.service;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;

public interface KiteConnectService {

    void kiteConnect(String token) throws KiteException;
}
