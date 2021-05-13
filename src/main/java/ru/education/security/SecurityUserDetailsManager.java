package ru.education.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SecurityUserDetailsManager implements UserDetailsManager {
    @Override
    public void createUser(UserDetails userDetails) {

    }

    @Override
    public void updateUser(UserDetails userDetails) {

    }

    @Override
    public void deleteUser(String s) {

    }

    @Override
    public void changePassword(String s, String s1) {

    }

    @Override
    public boolean userExists(String s) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // тут должна быть реализованна логика по вычитке пользователя из хранилища данных
        if (!userName.equals("user")) {
            return null;
        }

        List<SecurityPermission> permisions = new ArrayList<>();
        permisions.add(new SecurityPermission("product.read"));
        permisions.add(new SecurityPermission("product.readById"));
        permisions.add(new SecurityPermission("product.create"));
        permisions.add(new SecurityPermission("product.update"));

        return new SecurityUser("user", "123", permisions);
    }

}
