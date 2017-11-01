package io.microsamples.venueshare.venue.domain;

import lombok.ToString;
import org.springframework.util.Assert;

import java.time.Instant;


@ToString
public class AttendeeEvent implements DomainEvent{

    public AttendeeEvent(Attendee attendee) {
        Assert.notNull(attendee, "Valid attendee needed.");
        this.attendee = attendee;
        this.when = Instant.now();
    }

    public Object getData() {
        return attendee;
    }

    @Override
    public Instant occuredAt() {
        return when;
    }
    private final Attendee attendee;
    private final Instant when;
}
