package com.softserve.itacademy.kek.controller;

import com.softserve.itacademy.kek.controller.utils.KekMediaType;
import com.softserve.itacademy.kek.dto.AddressDto;
import com.softserve.itacademy.kek.dto.AddressListDto;
import com.softserve.itacademy.kek.dto.DetailsDto;
import com.softserve.itacademy.kek.dto.UserDto;
import com.softserve.itacademy.kek.dto.UserListDto;
import com.softserve.itacademy.kek.models.IAddress;
import com.softserve.itacademy.kek.models.IUser;
import com.softserve.itacademy.kek.services.IAddressService;
import com.softserve.itacademy.kek.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
public class UserController extends DefaultController {
    final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final IUserService userService;
    private final IAddressService addressService;

    @Autowired
    public UserController(IUserService userService, IAddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    private UserDto transformUser(IUser user) {
        DetailsDto userDetailsDto = new DetailsDto(user.getUserDetails().getPayload(), user.getUserDetails().getImageUrl());
        UserDto userDto = new UserDto(user.getGuid(), user.getName(), user.getNickname(), user.getEmail(),
                user.getPhoneNumber(), userDetailsDto);
        return userDto;
    }

    private AddressDto transformAddress(IAddress address) {
        return new AddressDto(address.getGuid(), address.getAlias(), address.getAddress(), address.getNotes());
    }

    /**
     * Get information about users
     *
     * @return Response entity with list of {@link UserListDto} objects as a JSON
     */
    @GetMapping(produces = KekMediaType.USER_LIST)
    public ResponseEntity<UserListDto> getUserList() {
        logger.info("Client requested the list of all users");

        List<IUser> userList = userService.getAll();
        UserListDto userListDto = new UserListDto(userList
                .stream()
                .map(this::transformUser)
                .collect(Collectors.toList()));

        logger.info("Sending list of all users to the client:\n{}", userListDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userListDto);
    }

    /**
     * Creates a new user
     *
     * @param newUserDto {@link UserListDto} object as a JSON
     * @return Response entity with {@link UserDto} object as a JSON
     */
    @PostMapping(consumes = KekMediaType.USER,
            produces = KekMediaType.USER)
    public ResponseEntity<UserDto> addUser(@RequestBody @Valid UserDto newUserDto) {
        logger.info("Accepted requested to create a new user:\n{}", newUserDto);

        IUser createdUser = userService.create(newUserDto);
        UserDto createdUserDto = transformUser(createdUser);

        logger.info("Sending the created users to the client:\n{}", createdUserDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUserDto);
    }

    /**
     * Returns information about the requested user
     *
     * @param guid user ID from the URN
     * @return Response entity with {@link UserDto} object as a JSON
     */
    @GetMapping(value = "/{guid}", produces = KekMediaType.USER)
    public ResponseEntity<UserDto> getUser(@PathVariable String guid) {
        logger.info("Client requested the user {}", guid);

        IUser user = userService.getByGuid(UUID.fromString(guid));
        UserDto userDto = transformUser(user);

        logger.info("Sending the user to the client:\n{}", userDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDto);
    }

    /**
     * Modifies information of the specified user
     *
     * @param guid user guid from the URN
     * @param user user object as a JSON
     * @return Response entity with {@link UserDto} object as a JSON
     */
    @PutMapping(value = "/{guid}",
            consumes = KekMediaType.USER,
            produces = KekMediaType.USER)
    public ResponseEntity<UserDto> modifyUser(@PathVariable String guid, @RequestBody @Valid UserDto user) {
        logger.info("Accepted modified user from the client:\n{}", user);

        IUser modifiedUser = userService.update(user);
        UserDto modifiedUserDto = transformUser(modifiedUser);

        logger.info("Sending the modified user to the client:\n{}", modifiedUserDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modifiedUserDto);
    }

    /**
     * Removes the specified user
     *
     * @param guid user guid from the URN
     */
    @DeleteMapping("/{guid}")
    public ResponseEntity deleteUser(@PathVariable String guid) {
        logger.info("Accepted request to delete the user {}", guid);

        userService.deleteByGuid(UUID.fromString(guid));

        logger.info("User ({}) successfully deleted", guid);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * Finds addresses of the specific user
     *
     * @param guid user guid from the URN
     * @return Response Entity with list of the {@link AddressListDto} objects as a JSON
     */
    @GetMapping(value = "/{guid}/addresses", produces = KekMediaType.ADDRESS_LIST)
    public ResponseEntity<AddressListDto> getUserAddresses(@PathVariable String guid) {
        logger.info("Client requested all the addresses of the employee {}", guid);

        List<IAddress> addresses = addressService.getAllForUser(UUID.fromString(guid));
        AddressListDto addressListDto = new AddressListDto(addresses
                .stream()
                .map(this::transformAddress)
                .collect(Collectors.toList()));

        logger.info("Sending the list of addresses of the user {} to the client:\n", addressListDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addressListDto);
    }

    /**
     * Adds a new addresses for the specific user
     *
     * @param guid            user guid from the URN
     * @param newAddressesDto list of address objects as a JSON
     * @return Response entity with list of the {@link AddressListDto} objects as a JSON
     */
    @PostMapping(value = "/{guid}/addresses",
            consumes = KekMediaType.ADDRESS_LIST,
            produces = KekMediaType.ADDRESS_LIST)
    public ResponseEntity<AddressListDto> addUserAddresses(@PathVariable String guid,
                                                           @RequestBody @Valid AddressListDto newAddressesDto) {
        logger.info("Accepted requested to create a new addresses for user:{}:\n", newAddressesDto);
        AddressListDto createdAddresses = new AddressListDto();

        for (AddressDto newAddress : newAddressesDto.getAddressList()) {
            IAddress createdAddress = addressService.createForUser(newAddress, UUID.fromString(guid));
            AddressDto addressDto = transformAddress(createdAddress);

            createdAddresses.addAddress(addressDto);
        }

        logger.info("Sending the created users's addresses to the client:\n{}", createdAddresses);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdAddresses);
    }

    /**
     * Finds addresses of the specific user
     *
     * @param guid     user guid from the URN
     * @param addrGuid address guid from the URN
     * @return Response entity with {@link AddressDto} object as a JSON
     */
    @GetMapping(value = "/{guid}/addresses/{addrguid}", produces = KekMediaType.ADDRESS)
    public ResponseEntity<AddressDto> getUserAddress(@PathVariable("guid") String guid,
                                                     @PathVariable("addrguid") String addrGuid) {
        logger.info("Client requested the address {} of the employee {}", addrGuid, guid);

        IAddress address = addressService.getForUser(UUID.fromString(addrGuid), UUID.fromString(guid));
        AddressDto addressDto = transformAddress(address);

        logger.info("Sending the address of the user {} to the client:\n{}", guid, addressDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addressDto);
    }

    /**
     * Modifies the specific user address tenant property
     *
     * @param guid       user guid from the URN
     * @param addrGuid   address guid from the URN
     * @param addressDto address object as a JSON
     * @return Response Entity with {@link AddressDto} object as a JSON
     */
    @PutMapping(value = "/{guid}/addresses/{addrguid}",
            consumes = KekMediaType.ADDRESS,
            produces = KekMediaType.ADDRESS)
    public ResponseEntity<AddressDto> modifyUserAddress(@PathVariable("guid") String guid,
                                                        @PathVariable("addrguid") String addrGuid,
                                                        @RequestBody @Valid AddressDto addressDto) {
        logger.info("Accepted modified address of the user {} from the client:\n{}", guid, addressDto);

        IAddress modifiedAddress = addressService.updateForUser(addressDto, UUID.fromString(guid));
        AddressDto modifiedAddressDto = transformAddress(modifiedAddress);

        logger.info("Sending the modified address of the user {} to the client:\n{}", guid, modifiedAddressDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modifiedAddressDto);
    }

    /**
     * Deletes the specific user address
     *
     * @param guid     user ID from the URN
     * @param addrGuid address ID from the URN
     */
    @DeleteMapping("/{guid}/addresses/{addrguid}")
    public ResponseEntity deleteUserAddress(@PathVariable("guid") String guid,
                                            @PathVariable("addrguid") String addrGuid) {
        logger.info("Accepted request to delete the address {} ot the user {}", addrGuid, guid);

        addressService.deleteForUser(UUID.fromString(addrGuid), UUID.fromString(guid));

        logger.info("The address {} ot the user {} successfully deleted", addrGuid, guid);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
