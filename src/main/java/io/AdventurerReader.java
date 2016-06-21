package io;

import domain.Adventurer;

import java.io.IOException;
import java.util.List;

/**
 * Created by shn on 20/06/2016.
 */
public interface AdventurerReader {

    void read() throws IOException;

    List<Adventurer> getAdventurers();
}
