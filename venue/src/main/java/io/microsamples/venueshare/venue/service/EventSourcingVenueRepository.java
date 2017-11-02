package io.microsamples.venueshare.venue.service;

import io.microsamples.venueshare.venue.domain.DomainEvent;
import io.microsamples.venueshare.venue.domain.Venue;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class EventSourcingVenueRepository {
    Optional<Venue> find(UUID venueId) {

        Assert.notNull(venueId, "Venue id needs to be provided.");

        Assert.isTrue(venues.containsKey(venueId), "Can not find specified venue : " + venueId.toString());

        return Optional.of(Venue.recreateFrom(venueId, venues.get(venueId)));
    }

    void save(Venue venue) {

        List<DomainEvent> newChanges = venue.getChanges();
        List<DomainEvent> currentChanges = venues.getOrDefault(venue.id(), new ArrayList<>());
        currentChanges.addAll(newChanges);
        venues.put(venue.id(), currentChanges);
//        venue.flushChanges();
//        newChanges.forEach(eventPublisher::sendEvent);

    }

    private Map<UUID, List<DomainEvent>> venues = new ConcurrentHashMap<>();

    Optional<Venue> findAt(UUID venueId, Instant atOrBefore) {

        Assert.notNull(venueId, "Venue id needs to be provided.");
        Assert.notNull(atOrBefore, "As of Time needs to be provided.");
        Assert.isTrue(venues.containsKey(venueId), "Can not find specified venue : " + venueId.toString());

        List<DomainEvent> events = venues.get(venueId).stream().filter(event -> !event.occuredAt().isAfter(atOrBefore))
                .collect(Collectors.toList());

        return Optional.of(Venue.recreateFrom(venueId, events));


    }
}
