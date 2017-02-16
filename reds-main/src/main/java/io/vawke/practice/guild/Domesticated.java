package io.vawke.practice.guild;

import io.vawke.practice.data.RegistryObject;

import java.util.UUID;

/**
 * Created by Giovanni on 12-2-2017.
 */
public interface Domesticated extends RegistryObject {

    UUID getOwnerUniqueId();

    String getOwnerName();
}
