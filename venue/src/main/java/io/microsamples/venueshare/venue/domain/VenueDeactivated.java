package io.microsamples.venueshare.venue.domain;

import java.time.Instant;

public class VenueDeactivated implements DomainEvent {

    public VenueDeactivated(Instant when) {
        this.when = when;
    }

    @Override
    public Instant occuredAt() {
        return when;
    }

    private final Instant when;
}
