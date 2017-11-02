package io.microsamples.venueshare.venue.domain;


import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

public class VenueTests {

    private Venue venue;
    private UUID venueId;

    @Before
    public void setUp() {
        venueId = UUID.randomUUID();
        venue = new Venue(venueId);
    }

    @Test
    public void should_create_venue_from_events() {

        UUID toLeave = UUID.randomUUID();
        DomainEvent[] domainEvents = {
                new VenueActivated(Instant.now())
                , new VenueNameChanged("Christmass Celebration")
                , new AttendeeJoined(Attendee.of(toLeave))
                , new AttendeeJoined(Attendee.of(UUID.randomUUID()))
                , new AttendeeLeft(Attendee.of(toLeave))};

        Venue venue = Venue.recreateFrom(venueId, Arrays.asList(domainEvents));

        assertThat(venue.isActive(), is(true));

    }

    @Test
    public void should_issue_event_when_attendee_joins() {

        Attendee attendee = Attendee.of(UUID.randomUUID());

        venue.activate();
        venue.attend(attendee);

        assertThat(venue.getChanges(), hasItem(isA(AttendeeJoined.class)));
        assertThat(venue.getChanges(), hasItem(
                hasProperty("data", is(attendee))
        ));
    }

    @Test
    public void should_issue_event_when_attendee_leaves() {
        Attendee attendee = Attendee.of(UUID.randomUUID());

        venue.activate();
        venue.leave(attendee);

        assertThat(venue.getChanges(), hasItem(isA(AttendeeLeft.class)));
        assertThat(venue.getChanges(), hasItem(
                hasProperty("data", is(attendee))
        ));
    }

    @Test
    public void should_be_incactive_upon_creation() {
        assertThat(venue.isActive(), is(equalTo(false)));
    }

    @Test
    public void should_be_active_when_activated() {
        venue.activate();

        assertThat(venue.isActive(), is(equalTo(true)));
    }

    @Test
    public void should_be_inactive_when_deactivated() {
        venue.activate();

        venue.deActivate();

        assertThat(venue.isActive(), is(equalTo(false)));
    }
}
