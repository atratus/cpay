package trsit.cpay.web.edit;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Builder;

@Builder
@Data

public class EventMemberItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private UserViewBean user;
    private BigDecimal paymentValue;

}