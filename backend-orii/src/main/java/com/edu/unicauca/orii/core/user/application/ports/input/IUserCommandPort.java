package com.edu.unicauca.orii.core.user.application.ports.input;

import com.edu.unicauca.orii.core.user.domain.model.User;

public interface IUserCommandPort {
    
    public User createUser(User user);

     /**
     * Updates an existing {@link User} by its ID.
     * 
     * @param id The ID of the user to update.
     * @param user The {@link User} object containing the updated data.
     * @return The updated {@link User}.
     */
    public User updateUser(Long id, User user);

    public boolean forgotPassword(String email);

    public void deleteUser(Long userId);
}
