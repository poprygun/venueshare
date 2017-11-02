package io.microsamples.venueshare.venue.service;

import io.microsamples.venueshare.venue.domain.Venue;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EventSourcingVenueRepositoryTests {

    private EventSourcingVenueRepository eventSourcingVenueRepository;
    private UUID venueId;

    @Before
    public void setUp(){
        eventSourcingVenueRepository = new EventSourcingVenueRepository();
        venueId = UUID.randomUUID();
    }

    @Test
    public void should_create_venue_at_time() throws InterruptedException {

        String expectedName = "The New Years Celebration";

        Venue venue = new Venue(venueId);
        venue.activate();
        venue.setName(expectedName);

        eventSourcingVenueRepository.save(venue);

        Thread.sleep(1500L);

        venue.deActivate();
        venue.setName("Christmass Celebration");

        eventSourcingVenueRepository.save(venue);

        Venue venueBeforechange
                = eventSourcingVenueRepository.findAt(venueId, Instant.now().minusMillis(1502L)).get();

        assertThat(venueBeforechange.getName(), is(expectedName));
    }

    @Test
    public void should_find_venue_constructed_from_events(){

        Venue venue = new Venue(venueId);
        venue.activate();
        String expectedName = "Hanukkah";
        venue.setName(expectedName);

        eventSourcingVenueRepository.save(venue);

        venue = eventSourcingVenueRepository.find(venueId).get();
        assertThat(venue, is(notNullValue()));
        assertThat(venue.getName(), is(expectedName));

    }

    @Test
    public void should_save_venue(){
        Venue venue = new Venue(venueId);
        venue.activate();

        eventSourcingVenueRepository.save(venue);

        venue = eventSourcingVenueRepository.find(venueId).get();
        assertThat(venue, is(notNullValue()));
        assertThat(venue.id(), is(equalTo(venueId)));
        assertThat(venue.isActive(), is(true));
    }
}
