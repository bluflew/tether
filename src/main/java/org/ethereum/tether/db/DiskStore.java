package org.ethereum.tether.db;

/**
 * @author Mikhail Kalinin
 * @since 09.07.2015
 */
public interface DiskStore {

    void open();

    void close();
}
