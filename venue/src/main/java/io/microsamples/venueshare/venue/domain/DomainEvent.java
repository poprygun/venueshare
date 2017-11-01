package io.microsamples.venueshare.venue.domain;

import java.time.Instant;

public interface DomainEvent {
    Instant occuredAt();
}
