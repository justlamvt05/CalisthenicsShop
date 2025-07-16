package lamthoncoding.shop.service;

import lamthoncoding.shop.entity.Address;

import java.util.List;
import java.util.Optional;

public interface AddressService {
    void addAddress(Address address);
    void updateAddress(Address address);
    Optional<Address> getAddressById(int id);

    List<Address> getAddressesByUserId(int id);

    void addOrUpdateAddress(Address address);
}
