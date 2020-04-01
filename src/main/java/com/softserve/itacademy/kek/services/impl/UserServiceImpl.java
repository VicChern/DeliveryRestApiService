package com.softserve.itacademy.kek.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softserve.itacademy.kek.exception.UserServiceException;
import com.softserve.itacademy.kek.models.IActor;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.models.IUserDetails;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.models.impl.UserDetails;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IUserService;

@Service
public class UserServiceImpl implements IUserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private TenantRepository tenantRepository;
    private ActorRepository actorRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TenantRepository tenantRepository, ActorRepository actorRepository) {
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.actorRepository = actorRepository;
    }

    @Transactional
    @Override
    public IUser create(IUser userData) throws UserServiceException {
        LOGGER.info("Insert User into DB: {}", userData);

        final User user = new User();

        user.setGuid(UUID.randomUUID());
        user.setName(userData.getName());
        user.setNickname(userData.getNickname());
        user.setEmail(userData.getEmail());
        user.setPhoneNumber(userData.getPhoneNumber());

        final UserDetails details = new UserDetails();
        user.setUserDetails(details);

        final IUserDetails detailsData = userData.getUserDetails();
        if (detailsData != null) {
            details.setImageUrl(detailsData.getImageUrl());
            details.setPayload(detailsData.getPayload());
        }

        try {
            final User userFromDB = userRepository.saveAndFlush(user);

            LOGGER.debug("User was inserted into DB: {}", userFromDB);

            return userFromDB;
        } catch (DataAccessException ex) {
            LOGGER.error("User was not inserted into DB: " + user, ex);
            throw new UserServiceException("An error occurred while inserting the user into the Database", ex);
        }
    }

    @Transactional
    @Override
    public IUser update(IUser userData) throws UserServiceException {
        LOGGER.info("Update User in DB: {}", userData);

        final User user = internalGetByGuid(userData.getGuid());

        user.setName(userData.getName());
        user.setNickname(userData.getNickname());
        user.setEmail(userData.getEmail());
        user.setPhoneNumber(userData.getPhoneNumber());

        final IUserDetails detailsData = userData.getUserDetails();
        if (detailsData != null) {
            final UserDetails details = new UserDetails();
            user.setUserDetails(details);

            details.setIdUser(user.getIdUser());
            details.setImageUrl(detailsData.getImageUrl());
            details.setPayload(detailsData.getPayload());
        }

        try {
            final User userFromDB = userRepository.saveAndFlush(user);

            LOGGER.debug("User was updated in DB: {}", userFromDB);

            return userFromDB;
        } catch (DataAccessException ex) {
            LOGGER.error("User was not updated in DB: " + user, ex);
            throw new UserServiceException("An error occurred while updating the user in the Database", ex);
        }
    }

    @Transactional
    @Override
    public void deleteByGuid(UUID guid) throws UserServiceException {
        LOGGER.info("Delete User from DB: guid = {}", guid);

        final User user = internalGetByGuid(guid);

        try {
            userRepository.deleteById(user.getIdUser());
            userRepository.flush();

            LOGGER.debug("User was deleted from DB: {}", user);
        } catch (DataAccessException ex) {
            LOGGER.error("User was not deleted from DB: " + user, ex);
            throw new UserServiceException("An error occurred while deleting the user from the Database", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public IUser getByGuid(UUID guid) throws UserServiceException {
        LOGGER.info("Get User from DB by guid: guid = {}", guid);
        return internalGetByGuid(guid);
    }

    @Transactional(readOnly = true)
    @Override
    public List<IUser> getAll() throws UserServiceException {
        LOGGER.info("Get all Users");

        try {
            final List<? extends IUser> users = userRepository.findAll();
            return (List<IUser>) users;
        } catch (DataAccessException ex) {
            LOGGER.error("An error while getting all users from DB", ex);
            throw new UserServiceException("An error occurred while getting users from the Database", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<IUser> getAll(Pageable pageable) throws UserServiceException {
        LOGGER.info("Getting a page of Users from DB: {}", pageable);

        try {
            final Page<? extends IUser> users = userRepository.findAll(pageable);
            return (Page<IUser>) users;
        } catch (DataAccessException ex) {
            LOGGER.error("An error while getting a page of users from DB", ex);
            throw new UserServiceException("An error occurred while getting users from the Database", ex);
        }
    }

    private User internalGetByGuid(UUID guid) throws UserServiceException {
        LOGGER.debug("Find User in DB: guid = {}", guid);

        try {
            final User user = userRepository.findByGuid(guid).orElseThrow(
                    () -> {
                        LOGGER.error("User wasn't found in the database");
                        return new UserServiceException("User was not found in database for guid: " + guid, new NoSuchElementException());
                    }
            );
            return user;

        } catch (DataAccessException ex) {
            LOGGER.error("DB exception for User {}", guid);
            throw new UserServiceException("An error occurred while getting user from the Database", ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<? extends GrantedAuthority> getUserAuthorities(String email) {
        LOGGER.info("Find User in DB: email = {}", email);

        final List<GrantedAuthority> authorityList = new ArrayList<>();

        final IUser user = userRepository.findByEmail(email);
        if (user == null) {
            LOGGER.warn("User wasn't found in DB: email = {}", email);
            return authorityList;
        } else {
            authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        }


        final ITenant tenant = tenantRepository.findByTenantOwner(user);
        if (tenant != null) {
            authorityList.add(new SimpleGrantedAuthority("ROLE_TENANT"));
        }

        final IActor actor = actorRepository.findByUser(user);
        if (actor != null) {
            authorityList.add(new SimpleGrantedAuthority("ROLE_ACTOR"));
        }
        return authorityList;


    }

}
