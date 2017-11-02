package io.microsamples.venueshare.venue.domain;

import javaslang.API;
import javaslang.Predicates;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static javaslang.collection.List.ofAll;


import static javaslang.API.Case;

public class Venue {

    public Venue(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID id(){
        return uuid;
    }



    public static Venue recreateFrom(UUID uuid, List<DomainEvent> domainEvents) {
        return ofAll(domainEvents).foldLeft(new Venue(uuid), Venue::handleEvent);
    }

    Venue handleEvent(DomainEvent event) {
        return API.Match(event).of(
                Case(Predicates.instanceOf(AttendeeEvent.class), this::queueAttendeeEvent)
               , Case(Predicates.instanceOf(VenueActivated.class), this::venueActivated)
               , Case(Predicates.instanceOf(VenueDeactivated.class), this::venueDeactivated)
               , Case(Predicates.instanceOf(VenueNameChanged.class), this::nameChanged)
        );
    }

    public void activate() {
        if (isActive()) throw new IllegalStateException();

        venueActivated(new VenueActivated(Instant.now()));
    }

    public boolean isActive() {
        return state.equals(VenueState.ACTIVATED);
    }
    private Venue nameChanged(VenueNameChanged venueNameChanged) {
        name = venueNameChanged.getName();
        changes.add(venueNameChanged);
        return this;
    }

    private Venue venueActivated(VenueActivated venueActivated) {
        state = VenueState.ACTIVATED;
        changes.add(venueActivated);
        return this;
    }


    public void deActivate() {
        if (!isActive()) throw new IllegalStateException();

        venueDeactivated(new VenueDeactivated(Instant.now()));
    }

    private Venue venueDeactivated(VenueDeactivated venueDeactivated) {
        state = VenueState.DEACTIVATED;
        changes.add(venueDeactivated);
        return this;
    }

    void attend(Attendee attendee) {
        if (!isActive()) throw new IllegalStateException("Venue is not active.");

        queueAttendeeEvent(new AttendeeJoined(attendee));
    }

    public List<DomainEvent> getChanges() {
        return Collections.unmodifiableList(changes);
    }

    public void flushChanges() {
        changes.clear();
    }

    void leave(Attendee attendee) {
        if (!isActive()) throw new IllegalStateException("Venue is not active.");
        queueAttendeeEvent(new AttendeeLeft(attendee));
    }

    private Venue queueAttendeeEvent(AttendeeEvent attendeeLeft) {
        changes.add(attendeeLeft);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        nameChanged(new VenueNameChanged(name));
    }


    enum VenueState {
        ACTIVATED, DEACTIVATED
    }

    private VenueState state = VenueState.DEACTIVATED;
    private List<DomainEvent> changes = new ArrayList<>();
    private final UUID uuid;
    private String name;

}
