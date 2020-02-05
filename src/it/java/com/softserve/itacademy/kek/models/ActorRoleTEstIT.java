package com.softserve.itacademy.kek.models;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;
import com.softserve.itacademy.kek.utils.ITTestUtils;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Component
@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class ActorRoleTEstIT extends AbstractTestNGSpringContextTests {

    public static final int MAX_NAME_LENGTH = 256;

    @Autowired
    private ActorRoleRepository actorRoleRepository;

    private ActorRole actorRole1;
    private ActorRole actorRole2;

    @BeforeClass
    public void setUp() {
        actorRole1 = ITTestUtils.getActorRole();
        actorRole2 = ITTestUtils.getActorRole();
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void whenNameSizeMoreThan256() {
        actorRole1.setName(RandomString.make(MAX_NAME_LENGTH + 1));

        actorRoleRepository.save(actorRole1);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void whenNameSizeLessThan1() {

    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void whenNameIsNull() {
        actorRole1.setName(null);

        actorRoleRepository.save(actorRole1);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void whenNameIsNotUnique() {
        actorRoleRepository.save(actorRole1);

        String name = actorRole1.getName();
        actorRole2.setName(name);

        actorRoleRepository.save(actorRole2);
    }

}
