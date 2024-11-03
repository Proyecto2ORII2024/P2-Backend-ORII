package com.edu.unicauca.orii.core.user.application.ports.output;

import com.edu.unicauca.orii.core.user.domain.model.User;

public interface IUserCommandPersistencePort {

    public User createUser(User user);

    /**
     * Updates an existing {@link User} in the database.
     * 
     * @param id The ID of the {@link User} to update.
     * @param user The {@link User} object containing the updated data.
     * @return The updated {@link User}.
     */
    public User updateUser(Long id, User user);

    public boolean forgotPassword(String email,String password);


    public void deleteUser(Long userId);

    public boolean existByEmail(String email);

    public void updatePassword(Long id, String newPassword);

}
