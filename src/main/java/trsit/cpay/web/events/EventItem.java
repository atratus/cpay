package trsit.cpay.web.events;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class EventItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private Date creationTimestamp;
}