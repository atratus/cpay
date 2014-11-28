package trsit.cpay.web.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import trsit.cpay.web.edit.EventMemberItem;
import lombok.Data;

@Data
public class EventView implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long eventId;
    private final List<EventMemberItem> eventItems = new ArrayList<>();
    private String eventTitle;   
}