package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.models.IActor;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;

/**
 * Service interface for {@link IActor}
 */
public interface IActorService {

    Actor saveActor(Tenant tenant, User user, ActorRole actorRole);
}
