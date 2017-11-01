package io.microsamples.venueshare.venue.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(staticName = "of")
public class Attendee {
    @NonNull
    @Getter
    private UUID id;
}
