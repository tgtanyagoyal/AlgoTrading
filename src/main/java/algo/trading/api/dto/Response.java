package algo.trading.api.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder
public class Response {

    private int code;
    private String message;
    private Object data;

}
