package org.ethereum.tether.util;

import java.util.ArrayList;

/**
 * @author Roman Mandeleil
 * @since 21.04.14
 */
public class RLPList extends ArrayList<RLPElement> implements RLPElement {

    /**
     *
     */
    private static final long serialVersionUID = 4295342919763885324L;
    byte[] rlpData;

    public static void recursivePrint(RLPElement element) {

        if (element == null)
            throw new RuntimeException("RLPElement object can't be null");
        if (element instanceof RLPList) {

            RLPList rlpList = (RLPList) element;
            System.out.print("[");
            for (RLPElement singleElement : rlpList)
                recursivePrint(singleElement);
            System.out.print("]");
        } else {
            String hex = ByteUtil.toHexString(element.getRLPData());
            System.out.print(hex + ", ");
        }
    }

    public byte[] getRLPData() {
        return rlpData;
    }

    public void setRLPData(byte[] rlpData) {
        this.rlpData = rlpData;
    }
}
