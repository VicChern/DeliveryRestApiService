package com.softserve.itacademy.kek.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.OrderEventType;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;


@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class WriteConstantsToDB extends AbstractTestNGSpringContextTests {

    @Autowired
    private ActorRoleRepository actorRoleRepository;

    @Autowired
    private OrderEventTypeRepository orderEventTypeRepository;

    private ActorRole actorRole1;
    private ActorRole actorRole2;

    private OrderEventType orderEventType1;
    private OrderEventType orderEventType2;
    private OrderEventType orderEventType3;
    private OrderEventType orderEventType4;


    @BeforeMethod(groups = {"integration-tests"})
    public void setUp() {

        actorRole1 = new ActorRole();
        actorRole1.setName("CUSTOMER");
        actorRole2 = new ActorRole();
        actorRole2.setName("CURRIER");

        orderEventType1 = new OrderEventType();
        orderEventType1.setName("CREATED");

        orderEventType2 = new OrderEventType();
        orderEventType2.setName("ASSIGNED");

        orderEventType3 = new OrderEventType();
        orderEventType3.setName("STARTED");

        orderEventType4 = new OrderEventType();
        orderEventType4.setName("DELIVERED");
    }

    @AfterMethod(groups = {"integration-tests"})
    public void tearDown() {
        actorRoleRepository.deleteAll();
        orderEventTypeRepository.deleteAll();
    }

    @Test(groups = {"integration-tests"})
    public void saveActorRolesAndOrderEventTypesToDb() {

        actorRoleRepository.save(actorRole1);
        actorRoleRepository.save(actorRole2);

        orderEventTypeRepository.save(orderEventType1);
        orderEventTypeRepository.save(orderEventType2);
        orderEventTypeRepository.save(orderEventType3);
        orderEventTypeRepository.save(orderEventType4);
    }
}
