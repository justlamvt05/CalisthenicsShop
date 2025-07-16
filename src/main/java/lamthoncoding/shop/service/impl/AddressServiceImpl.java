package lamthoncoding.shop.service.impl;

import lamthoncoding.shop.entity.Address;
import lamthoncoding.shop.repository.AddressRepository;
import lamthoncoding.shop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public void addAddress(Address address) {

        addressRepository.save(address);
    }

    @Override
    public void updateAddress(Address updatedAddress) {
        Optional<Address> optionalAddress = addressRepository.findById(updatedAddress.getId());

        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();
            address.setAddressLine(updatedAddress.getAddressLine());
            address.setCity(updatedAddress.getCity());
            address.setPhoneNumber(updatedAddress.getPhoneNumber());
            address.setPostalCode(updatedAddress.getPostalCode());
            address.setIsDefault(updatedAddress.getIsDefault());
            address.setStatus(updatedAddress.getStatus());

            addressRepository.save(address);
        }
    }

    @Override
    public Optional<Address> getAddressById(int id) {
        return addressRepository.findById(id);
    }

    @Override
    public List<Address> getAddressesByUserId(int userId) {
        return addressRepository.findByUserId((userId));
    }

    @Override
    public void addOrUpdateAddress(Address address) {
        addressRepository.save(address);
    }
}
