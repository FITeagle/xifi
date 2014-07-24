package org.fiteagle.dm.federation.auth;

/**
 * Created by dne on 09.07.14.
 */
public enum RoleEnum {
    IOMember,
    IOAdmin,
    FM,
    Provider;

    public static boolean contains(String test) {

        for (RoleEnum r : RoleEnum.values()) {
            if (r.name().equals(test)) {
                return true;
            }
        }

        return false;
    }
}