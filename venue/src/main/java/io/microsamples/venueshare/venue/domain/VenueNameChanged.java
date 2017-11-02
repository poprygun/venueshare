package io.microsamples.venueshare.venue.domain;

import java.time.Instant;

public class VenueNameChanged implements DomainEvent {
    public VenueNameChanged(String name) {
        this.name = name;
        when = Instant.now();

    }

    @Override
    public Instant occuredAt() {
        return when;
    }

    private final Instant when;
    private final String name;

    public String getName() {
        return name;
    }
}
