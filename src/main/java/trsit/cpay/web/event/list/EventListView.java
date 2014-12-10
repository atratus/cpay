package trsit.cpay.web.event.list;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class EventListView implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private Date creationTimestamp;
    private String eventType;
    private Long id;
}