package algo.trading.api.service.impl;

import algo.trading.api.config.KiteConnectConfig;
import algo.trading.api.service.KiteConnectService;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.models.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONException;
import java.io.IOException;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;

@Service
public class KiteConnectServiceImpl implements KiteConnectService {
    private final KiteConnect kiteConnect;
    private final String apiSecret;

    public KiteConnectServiceImpl(
            KiteConnect kiteConnect,
            @Value("${kite.apiSecret}") String apiSecret) {
        this.kiteConnect = kiteConnect;
        this.apiSecret = apiSecret;
    }

    @Override
    public void kiteConnect(String requestToken) throws KiteException {
        try {
            String url = kiteConnect.getLoginURL();

            kiteConnect.setSessionExpiryHook(new SessionExpiryHook() {
                @Override
                public void sessionExpired() {
                    System.out.println("session expired");
                }
            });

            User user =  kiteConnect.generateSession(requestToken, apiSecret);
            kiteConnect.setAccessToken(user.accessToken);
            kiteConnect.setPublicToken(user.publicToken);

            getProfile(kiteConnect);

        } catch (KiteException e) {
            System.out.println(e.message+" "+e.code+" "+e.getClass().getName());
            throw e;
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getProfile(KiteConnect kiteConnect) throws IOException, KiteException {
        Profile profile = kiteConnect.getProfile();
        System.out.println(profile.userName);
    }

}
