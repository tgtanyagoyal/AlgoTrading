package algo.trading.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Candle {

    public double high, low, close, volume;
    public Date timestamp;

    public Candle(double high, double low, double close, double volume, Date timestamp) {
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.timestamp = timestamp;
    }


}
