package algo.trading.api.service.impl;

import algo.trading.api.service.KiteConnectService;
import org.springframework.stereotype.Service;
import org.json.JSONException;
import java.io.IOException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;

@Service
public class KiteConnectServiceImpl implements KiteConnectService {

    @Override
    public void kiteConnect(String requestToken) throws KiteException {
        try {

            KiteConnect kiteConnect = new KiteConnect("kphy76pqgmz5zkw1");

            kiteConnect.setUserId("JWR763");

            String url = kiteConnect.getLoginURL();

            kiteConnect.setSessionExpiryHook(new SessionExpiryHook() {
                @Override
                public void sessionExpired() {
                    System.out.println("session expired");
                }
            });

            User user =  kiteConnect.generateSession(requestToken, "lstun8cx3d698wfc4aqsz941eqgiz1aw");
            kiteConnect.setAccessToken(user.accessToken);
            kiteConnect.setPublicToken(user.publicToken);

//            TestingService examples = new TestingService();

//            examples.getProfile(kiteConnect);

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

}
