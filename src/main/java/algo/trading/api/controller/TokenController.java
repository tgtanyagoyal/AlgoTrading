package algo.trading.api.controller;

import algo.trading.api.service.TokenService;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TokenController {
	private final TokenService tokenService;

	public TokenController(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@GetMapping("/token")
	public ResponseEntity<TokenResponse> handleToken(
			@RequestHeader("requestToken") String requestToken) throws KiteException {

		if (!StringUtils.hasText(requestToken)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new TokenResponse("", "requestToken is required"));
		}

		String result = tokenService.processToken(requestToken);
		return ResponseEntity.ok(new TokenResponse(requestToken, result));
	}

	public record TokenResponse(String requestToken, String result) {}
}
