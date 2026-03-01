package algo.trading.api.config;

import com.zerodhatech.kiteconnect.KiteConnect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KiteConnectConfig {
	@Bean
	public KiteConnect kiteConnect(
			@Value("${kite.apiKey}") String apiKey,
			@Value("${kite.userId}") String userId) {
		KiteConnect kiteConnect = new KiteConnect(apiKey);
		kiteConnect.setUserId(userId);
		return kiteConnect;
	}
}
