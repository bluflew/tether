package org.ethereum.tether.util;

/**
 * @author Roman Mandeleil
 * @since 21.04.14
 */
public class RLPItem implements RLPElement {

    /**
     *
     */
    private static final long serialVersionUID = 4570267401757053497L;
    private final byte[] rlpData;

    public RLPItem(byte[] rlpData) {
        this.rlpData = rlpData;
    }

    public byte[] getRLPData() {
        if (rlpData.length == 0)
            return null;
        return rlpData;
    }
}
