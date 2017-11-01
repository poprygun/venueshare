package io.microsamples.venueshare.venue.domain;

import java.time.Instant;

public class VenueActivated implements DomainEvent {

    public VenueActivated(Instant when) {
        this.when = when;
    }

    @Override
    public Instant occuredAt() {
        return when;
    }

    private final Instant when;
}
