package trsit.cpay.web.edit;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventMemberItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private UserViewBean user;
    private Integer paymentValue;

}