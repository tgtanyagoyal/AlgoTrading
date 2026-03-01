package algo.trading.api.controller;

import algo.trading.api.dto.Response;
import algo.trading.api.service.InstrumentService;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/api")
public class InstrumentController {

	private final InstrumentService instrumentService;

	public InstrumentController(InstrumentService instrumentService) {
		this.instrumentService = instrumentService;
	}

	@GetMapping("/v1/strategy/back/testing")
	public ResponseEntity<Response> handleInstrument(
			@RequestHeader(value = "instrumentToken", required = true) String instrumentToken,
			@RequestHeader(value = "startDate", required = false) String startDate,
			@RequestHeader(value = "endDate", required = false) String endDate) throws IOException, ParseException, KiteException {

//		if (!StringUtils.hasText(instrumentToken)
//				|| !StringUtils.hasText(startDate)
//				|| !StringUtils.hasText(endDate)) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(new Response(HttpStatus.BAD_REQUEST.value(), "instrumentToken, startDate, and endDate are required",null));
//		}

		String result = instrumentService.processRequest(instrumentToken, startDate, endDate);
		return ResponseEntity.ok(new Response(0,"Response fetched successfully",null));
	}
}
